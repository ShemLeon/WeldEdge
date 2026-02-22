# WeldEdge — Architecture & Flow Diagrams

## 1. High-Level Architecture (Layers)

```
┌─────────────────────────────────────────────────────────────────────────┐
│                         PRESENTATION LAYER                               │
│  MainActivity → MainScreen → MainScreenContent                           │
│  Components: WeldingForm, DocumentPreviewScreen, Header, etc.           │
└─────────────────────────────────────────────────────────────────────────┘
                                    │
                                    │ state / events
                                    ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                         VIEWMODEL LAYER                                  │
│  MainScreenViewModel (StateFlow<MainScreenState>, MainScreenEvent)        │
└─────────────────────────────────────────────────────────────────────────┘
                                    │
                                    │ use cases, preferences
                                    ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                         DOMAIN LAYER                                     │
│  GenerateReportUseCase, Models (WeldingParams, EdgePreparation, etc.)    │
└─────────────────────────────────────────────────────────────────────────┘
                                    │
                                    │ repository interface
                                    ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                         DATA LAYER                                       │
│  ReportRepositoryImpl, AlloysDatabaseRepository, PreferencesManager     │
└─────────────────────────────────────────────────────────────────────────┘
```

---

## 2. Component Diagram (Mermaid)

```mermaid
flowchart TB
    subgraph Presentation["Presentation (Compose UI)"]
        MA[MainActivity]
        MS[MainScreen]
        MSC[MainScreenContent]
        HF[Header]
        WF[WeldingForm]
        DPS[DocumentPreviewScreen]
        
        subgraph WeldingFormComponents["WeldingForm Components"]
            MetalAlloy[MetalAlloy]
            Thickness[Thickness]
            JointType[JointTypeSelection]
            TypeOfWeld[TypeOfWeldsSelection]
            EdgePrep[EdgePreparationSelection]
            WeldingType[WeldingTypeSelection]
            Submit[SubmitButton]
        end
    end
    
    subgraph ViewModel["ViewModel"]
        VM[MainScreenViewModel]
    end
    
    subgraph Domain["Domain"]
        GRU[GenerateReportUseCase]
        WP[WeldingParams]
        EP[EdgePreparation]
        WT[WeldingType]
        JT[JointType]
        TOW[TypeOfWelds]
        Alloys[Alloys]
        WpsTable[WpsTable]
    end
    
    subgraph Data["Data"]
        RRI[ReportRepositoryImpl]
        RR[ReportRepository]
        ADR[AlloysDatabaseRepository]
        PM[PreferencesManager]
        RM[ResourceManager]
    end
    
    subgraph Drawer["PDF Generation"]
        WRD[WpsReportDrawer]
        TD[TableDrawer]
    end
    
    MA --> MS
    MS --> MSC
    MSC --> HF
    MSC --> WF
    MSC --> DPS
    WF --> MetalAlloy
    WF --> Thickness
    WF --> JointType
    WF --> TypeOfWeld
    WF --> EdgePrep
    WF --> WeldingType
    WF --> Submit
    
    MS -->|state, onEvent| VM
    VM -->|GenerateReportUseCase| GRU
    VM --> PM
    VM --> RM
    
    GRU --> RR
    RR -.->|implements| RRI
    RRI --> WRD
    WRD --> TD
    WRD --> WP
    
    MetalAlloy --> Alloys
    EdgePrep --> EP
    WP --> EP
    WP --> Alloys
    WP --> WpsTable
    WP --> WT
```

---

## 3. Event Flow (User Action → State Update)

```mermaid
sequenceDiagram
    participant User
    participant UI as WeldingForm / Components
    participant VM as MainScreenViewModel
    participant PM as PreferencesManager
    participant GRU as GenerateReportUseCase
    participant RR as ReportRepository

    User->>UI: Select thickness "2"
    UI->>VM: onEvent(ThicknessChanged("2"))
    VM->>VM: onThicknessChanged
    VM->>VM: Validate edge prep (clear if invalid)
    VM->>VM: _state.update(params.thickness = "2")
    
    User->>UI: Select edge preparation
    UI->>VM: onEvent(EdgePreparationChanged(id))
    VM->>VM: Validate (reject Double V if thickness < 6)
    VM->>VM: _state.update(params.edgePreparation = id)
    
    User->>UI: Click Submit
    UI->>VM: onEvent(SubmitClicked)
    VM->>VM: _state.update(DataPreview)
    
    User->>UI: Click Generate PDF
    UI->>VM: onEvent(GeneratePdfClicked)
    VM->>GRU: invoke(params)
    GRU->>RR: generateAndOpenReport(params)
    RR->>RR: Create PDF, open in viewer
```

---

## 4. State Flow (MainScreenState)

```mermaid
stateDiagram-v2
    [*] --> DataSelector: Initial load
    DataSelector --> DataSelector: Field changes (MetalType, Thickness, etc.)
    DataSelector --> DataPreview: SubmitClicked
    DataPreview --> DataSelector: BackClicked
    
    note right of DataSelector
        WeldingForm visible
        params editable
    end note
    
    note right of DataPreview
        DocumentPreviewScreen visible
        params read-only
        Generate PDF button
    end note
```

---

## 5. Dependency Injection (Hilt)

```mermaid
flowchart LR
    subgraph AppModule
        PM_Prov[providePreferencesManager]
        ADR_Prov[provideAlloysDatabaseRepository]
        RR_Prov[provideReportRepository]
        GRU_Prov[provideGenerateReportUseCase]
    end
    
    subgraph Injectees
        VM[MainScreenViewModel]
    end
    
    Context[Context] --> PM_Prov
    Context --> ADR_Prov
    RRI[ReportRepositoryImpl] --> RR_Prov
    RR_Prov --> GRU_Prov
    
    PM_Prov --> VM
    GRU_Prov --> VM
    RM[ResourceManager] --> VM
    
    RR[ReportRepository] -.-> RRI
```

---

## 6. Domain Models & Relationships

```mermaid
erDiagram
    WeldingParams ||--o| Alloy : "metalType"
    WeldingParams ||--o| Alloy : "metalType2"
    WeldingParams ||--o| EdgePreparation : "edgePreparation"
    WeldingParams ||--o| WeldingType : "weldingType"
    WeldingParams ||--o| JointType : "jointType"
    WeldingParams ||--o| TypeOfWelds : "typeOfWeld"
    
    EdgePreparation ||--o| TypeOfWelds : "typeOfWelds"
    EdgePreparation ||--o| JointType : "jointType"
    
    Alloy ||--o| AlloyCategory : "category"
    
    WeldingParams {
        string metalType
        string metalType2
        string thickness
        string jointType
        string typeOfWeld
        string edgePreparation
        string weldingType
    }
    
    EdgePreparation {
        string id
        string displayName
        string jointType
    }
```

---

## 7. File Structure Overview

```
weldedge/
├── MainActivity.kt              # Entry, Compose setContent
├── di/
│   └── AppModule.kt             # Hilt: ReportRepository, UseCase, Preferences, AlloysDB
├── domain/
│   ├── model/
│   │   ├── WeldingParams.kt     # Core data, WPS calculations
│   │   ├── EdgePreparation.kt   # Enum + getForSelection filter
│   │   ├── WeldingType.kt
│   │   ├── JointType.kt
│   │   ├── TypeOfWelds.kt
│   │   ├── AlloyData.kt / Alloys (object)
│   │   ├── AlloysDatabase.kt
│   │   └── WpsTable.kt
│   ├── repository/
│   │   └── ReportRepository.kt  # Interface
│   └── usecase/
│       └── GenerateReportUseCase.kt
├── data/
│   ├── local/
│   │   ├── PreferencesManager.kt
│   │   └── ResourceManager.kt
│   └── repository/
│       ├── ReportRepositoryImpl.kt  # PDF generation
│       └── AlloysDatabaseRepository.kt
└── presentation/
    ├── screen/main/
    │   ├── MainScreen.kt
    │   ├── MainScreenViewModel.kt
    │   ├── MainScreenState.kt
    │   ├── MainScreenEvent.kt
    │   └── components/
    │       ├── main/           # WeldingForm, Thickness, EdgePrep, etc.
    │       └── prev/           # DocumentPreviewScreen
    └── drawer/
        ├── WpsReportDrawer.kt
        └── TableDrawer.kt
```

---

## 8. Database Schema (alloys_database.json + WPS Decision Table)

### Alloys Database (JSON: `assets/data/alloys_database.json`)

```mermaid
erDiagram
    AlloysDatabase ||--o{ AlloyGroup : "groups"
    AlloyGroup ||--o{ AlloySubgroup : "subgroups"
    AlloySubgroup ||--o{ AlloyGrade : "grades"
    
    AlloysDatabase {
        int version
    }
    
    AlloyGroup {
        string id "AL, CS, SS"
        string name "Aluminium, Carbon Steel, Stainless Steel"
    }
    
    AlloySubgroup {
        string id "1xxx, 5xxx, 5.3, PH..."
        string name
        string isoGroup "ISO 15608"
    }
    
    AlloyGrade {
        string name "AISI 316L, 6061, S235JR..."
        string chemicalComposition
        string awsName
        string gostName
        string preheating "- or temp °C"
    }
```

### WPS Decision Table (WpsTable / WpsEntry)

```mermaid
erDiagram
    WeldingParams }o--|| WpsEntry : "findWps"
    
    WpsEntry {
        double thicknessMin "mm"
        double thicknessMax "mm"
        string metal1 "AL, CS, SS"
        string metal2 "AL, CS, SS"
        string process "GTAW, GMAW, FCAW, LBW"
        list typeOfWeld "BW, FW"
        list jointTypes "butt, t_joint, corner, lap"
        string wpsNumber "3092, 3090..."
        string alloy1 "optional: 4130"
        string alloy2 "optional"
        string alloyGroup "optional: PH"
    }
```

### Иерархия Alloys Database (текстовая схема)

```
AlloysDatabase
├── version: Int
└── groups: List<AlloyGroup>
    └── AlloyGroup (id: AL | CS | SS)
        ├── id: String
        ├── name: String
        └── subgroups: List<AlloySubgroup>
            └── AlloySubgroup (1xxx, 5xxx, 5.3, PH...)
                ├── id: String
                ├── name: String
                ├── isoGroup: String? (ISO 15608)
                └── grades: List<AlloyGrade>
                    └── AlloyGrade
                        ├── name: String
                        ├── chemicalComposition: String
                        ├── awsName: String
                        ├── gostName: String
                        └── preheating: String
```

---

## 9. Data Flow Summary

| Source | → | Target |
|-------|---|--------|
| User input (UI) | → | MainScreenEvent |
| MainScreenEvent | → | MainScreenViewModel.onEvent |
| ViewModel | → | _state.update (MutableStateFlow) |
| StateFlow | → | MainScreen (collectAsState) |
| MainScreenState.params | → | WeldingForm, DocumentPreviewScreen |
| SubmitClicked | → | DataSelector → DataPreview |
| GeneratePdfClicked | → | GenerateReportUseCase → ReportRepositoryImpl → WpsReportDrawer |
