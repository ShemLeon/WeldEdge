"""
Convert PNG and SVG assets to WebP format.
Maps user's filenames to code-expected names for EdgePreparation and WeldingType.
Requires: pip install Pillow
For SVG: requires cairosvg (pip install cairosvg) or ImageMagick/Inkscape in PATH.
"""
import io
import os
import subprocess
import tempfile
from pathlib import Path

# Mapping: (src_dir, src_filename, dst_dir, dst_filename)
# Paths relative to assets/
ASSET_MAPPING = [
    # corner/simple (FW)
    ("edge_preparation/corner/simple", "Corner Flange.png", "edge_preparation/corner/simple", "corner_flange.webp"),
    ("edge_preparation/corner/simple", "Corner Flush.png", "edge_preparation/corner/simple", "corner_flush.webp"),
    ("edge_preparation/corner/simple", "Corner Full Open.png", "edge_preparation/corner/stress", "corner_full_open.webp"),
    ("edge_preparation/corner/simple", "Edge.png", "edge_preparation/corner/simple", "edge.webp"),
    ("edge_preparation/corner/simple", "Corner Flush.png", "edge_preparation/corner/simple", "flare_v.webp"),
    ("edge_preparation/corner/simple", "Corner Full Open.png", "edge_preparation/corner/stress", "corner_half_open.webp"),
    # corner/stress (BW)
    ("edge_preparation/corner/stress", "Corner Bevel Inside.png", "edge_preparation/corner/stress", "corner_bevel_inside.webp"),
    ("edge_preparation/corner/stress", "Corner Bevel Outside.png", "edge_preparation/corner/stress", "corner_bevel_outside.webp"),
    ("edge_preparation/corner/stress", "Corner V Groove.svg", "edge_preparation/corner/stress", "corner_v_groove.webp"),
    ("edge_preparation/corner/stress", "J-Groove Corner Outside.png", "edge_preparation/corner/stress", "corner_j_outside.webp"),
    ("edge_preparation/corner/stress", "Single-J-Groove Corner Outside.png", "edge_preparation/corner/stress", "corner_j_inside.webp"),
    ("edge_preparation/corner/stress", "U-Groove Corner.png", "edge_preparation/corner/stress", "corner_u.webp"),
    # groove/simple
    ("edge_preparation/groove/simple", "Square-Groove 1 Side.png", "edge_preparation/groove/simple", "groove_1_square_single.webp"),
    ("edge_preparation/groove/simple", "Square-Groove Both Sides.png", "edge_preparation/groove/simple", "groove_2_square_double.webp"),
    ("edge_preparation/groove/simple", "Flare-V Groove.png", "edge_preparation/groove/simple", "groove_3_flare_v.webp"),
    ("edge_preparation/groove/simple", "Flare-V Groove Both sides.png", "edge_preparation/groove/simple", "groove_4_flare_v_double.webp"),
    # groove/stress
    ("edge_preparation/groove/stress", "V-Groove Both sides.png", "edge_preparation/groove/stress", "groove_v_single.webp"),
    ("edge_preparation/groove/stress", "Double-V-Groove Both sides.png", "edge_preparation/groove/stress", "groove_v_double.webp"),
    ("edge_preparation/groove/stress", "Bevel-Groove 1 side.png", "edge_preparation/groove/stress", "groove_bevel.webp"),
    ("edge_preparation/groove/stress", "Bevel-Groove 1 side-1.png", "edge_preparation/groove/stress", "groove_bevel_double.webp"),
    ("edge_preparation/groove/stress", "J-Groove Both sides.png", "edge_preparation/groove/stress", "groove_j_double.webp"),
    ("edge_preparation/groove/stress", "J-Groove Both sides-1.png", "edge_preparation/groove/stress", "groove_j.webp"),
    ("edge_preparation/groove/stress", "Single-U-Groove.png", "edge_preparation/groove/stress", "groove_u.webp"),
    ("edge_preparation/groove/stress", "Single-U-Groove-1.png", "edge_preparation/groove/stress", "groove_u_double.webp"),
    # t/simple
    ("edge_preparation/t/simple", "Single-T-Joint-Fillet.png", "edge_preparation/t/simple", "t_joint_fillet.webp"),
    ("edge_preparation/t/simple", "Double-T-Joint-Fillet.png", "edge_preparation/t/simple", "t_joint_fillet_double.webp"),
    # t/stress (BW: bevel)
    ("edge_preparation/t/stress", "Single-T-Joint-Bevel.png", "edge_preparation/t/stress", "t_joint_bevel.webp"),
    ("edge_preparation/t/stress", "Single-T-Joint-Bevel-Support.png", "edge_preparation/t/stress", "t_joint_bevel_support.webp"),
    ("edge_preparation/t/stress", "Double-T-Joint-V.png", "edge_preparation/t/stress", "t_joint_bevel_double.webp"),
    # t/stress (BW: J-groove) - same files, also in stress for BW
    ("edge_preparation/t/stress", "Single-T-Joint-J.png", "edge_preparation/t/stress", "t_joint_j_groove.webp"),
    ("edge_preparation/t/stress", "Double-T-Joint-J-Groove.png", "edge_preparation/t/stress", "t_joint_j_groove_double.webp"),
    # t/simple (FW: flare bevel - copy to simple)
    ("edge_preparation/t/stress", "Single-T-Joint-J.png", "edge_preparation/t/simple", "t_joint_flare_bevel_groove.webp"),
    ("edge_preparation/t/stress", "Double-T-Joint-J-Groove.png", "edge_preparation/t/simple", "t_joint_flare_bevel_double_groove.webp"),
    # lap - bevel is stress (BW), fillet/spot/plug are simple (FW)
    ("edge_preparation/lap/simple", "Lap-Joint Bevel.png", "edge_preparation/lap/stress", "lap_joint_bevel.webp"),
    ("edge_preparation/lap/stress", "Lap-Joint fillet.png", "edge_preparation/lap/simple", "lap_joint_fillet.webp"),
    ("edge_preparation/lap/stress", "Slot.png", "edge_preparation/lap/simple", "lap_joint_plug_slot.webp"),
    ("edge_preparation/lap/stress", "Slot-1.png", "edge_preparation/lap/simple", "lap_joint_spot.webp"),
    # type_of_welding
    ("type_of_welding", "type_1_TIG.svg", "type_of_welding", "type_1_TIG.webp"),
    ("type_of_welding", "type_2_MAG-MIG.svg", "type_of_welding", "type_2_MAG-MIG.webp"),
    ("type_of_welding", "type_3_MMA.svg", "type_of_welding", "type_3_MMA.webp"),
    ("type_of_welding", "type_4_FCAW.svg", "type_of_welding", "type_4_FCAW.webp"),
]

ASSETS_ROOT = Path(__file__).parent / "app" / "src" / "main" / "assets"


def _svg_to_png_then_webp(src_path: Path, dst_path: Path, Image) -> bool:
    """Convert SVG to WebP via PNG. Tries cairosvg, then ImageMagick, then Inkscape."""
    # Try cairosvg first
    try:
        import cairosvg
        png_data = cairosvg.svg2png(url=str(src_path))
        img = Image.open(io.BytesIO(png_data))
        if img.mode in ("RGBA", "P"):
            img = img.convert("RGBA")
        else:
            img = img.convert("RGB")
        img.save(dst_path, "WEBP", quality=90)
        return True
    except Exception:
        pass
    # Try ImageMagick: magick input.svg output.webp
    try:
        subprocess.run(
            ["magick", str(src_path), "-quality", "90", str(dst_path)],
            check=True, capture_output=True
        )
        return True
    except (subprocess.CalledProcessError, FileNotFoundError):
        pass
    # Try Inkscape: inkscape input.svg -o output.png, then Pillow to WebP
    tmp_path = None
    try:
        with tempfile.NamedTemporaryFile(suffix=".png", delete=False) as tmp:
            tmp_path = Path(tmp.name)
        subprocess.run(
            ["inkscape", str(src_path), "-o", str(tmp_path)],
            check=True, capture_output=True
        )
        img = Image.open(tmp_path)
        if img.mode in ("RGBA", "P"):
            img = img.convert("RGBA")
        else:
            img = img.convert("RGB")
        img.save(dst_path, "WEBP", quality=90)
        return True
    except (subprocess.CalledProcessError, FileNotFoundError, Exception):
        pass
    finally:
        if tmp_path and tmp_path.exists():
            tmp_path.unlink(missing_ok=True)
    return False


def convert_to_webp():
    try:
        from PIL import Image
    except ImportError:
        print("Install Pillow: pip install Pillow")
        return False

    for src_dir, src_name, dst_dir, dst_name in ASSET_MAPPING:
        src_path = ASSETS_ROOT / src_dir / src_name
        dst_path = ASSETS_ROOT / dst_dir / dst_name
        dst_path.parent.mkdir(parents=True, exist_ok=True)

        if not src_path.exists():
            print(f"Skip (not found): {src_path}")
            continue

        try:
            if src_name.lower().endswith(".svg"):
                if _svg_to_png_then_webp(src_path, dst_path, Image):
                    print(f"OK: {src_name} -> {dst_name}")
                else:
                    print(f"Skip SVG (no converter): {src_path}")
            else:
                img = Image.open(src_path)
                if img.mode in ("RGBA", "P"):
                    img = img.convert("RGBA")
                else:
                    img = img.convert("RGB")
                img.save(dst_path, "WEBP", quality=90)
                print(f"OK: {src_name} -> {dst_name}")
        except Exception as e:
            print(f"Error {src_path}: {e}")

    return True


if __name__ == "__main__":
    import io
    convert_to_webp()
    print("\nDone. Update EdgePreparation.kt and WeldingType.kt to use .webp instead of .svg")
