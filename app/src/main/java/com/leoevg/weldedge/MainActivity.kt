package com.leoevg.weldedge

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leoevg.weldedge.domain.TableConfig
import com.leoevg.weldedge.presentation.drawer.drawSimpleTable
import com.leoevg.weldedge.ui.theme.WeldEdgeTheme
import java.io.File
import java.io.FileOutputStream

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeldEdgeTheme {
                Surface {
                    PdfGenerator()
                }
            }
        }
    }
}


@Composable
fun PdfGenerator() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title text
        Text(
            text = "PDF Generator",
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Button to generate the PDF
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            onClick = { generatePDF(context) }
        ) {
            Text(modifier = Modifier.padding(6.dp), text = "Generate PDF")
        }
    }
}

fun generatePDF(context: Context) {

    // Define the page height
    val pageHeight = 1120

    // Define the page width
    val pageWidth = 792

    // Create a new PDF document
    val pdfDocument = PdfDocument()

    // Create a PDF page with specified width and height
    val myPageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()
    val myPage = pdfDocument.startPage(myPageInfo)

    // Get the canvas to draw on
    val canvas: Canvas = myPage.canvas


    val table1Data = listOf(listOf("First column", "First column data"))
    val table1Header = "Header1"
    val table1Footer = "Footer1"
    canvas.drawSimpleTable(
        data = table1Data,
        headerTitle = table1Header,
        footerTitle = table1Footer,
        x = 10F,
        y = 10F,
        width = 300F
    )

    val table2Data = listOf(listOf("First column", "First column data"))
    val table2Header = "Header1"
    canvas.drawSimpleTable(
        data = table2Data,
        headerTitle = table2Header,
        x = 350F,
        y = 10F,
        width = 300F
    )

    // Finish writing to the page
    pdfDocument.finishPage(myPage)

    // Define a file path in the app's
    // private storage (no permissions required)
    val file = File(context.getExternalFilesDir(null), "GFG.pdf")

    try {

        // Write the PDF to the file
        pdfDocument.writeTo(FileOutputStream(file))
        Toast.makeText(context, "PDF saved at ${file.absolutePath}", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "Failed to generate PDF", Toast.LENGTH_SHORT).show()
    }

    // Close the document
    pdfDocument.close()
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WeldEdgeTheme {

    }
}