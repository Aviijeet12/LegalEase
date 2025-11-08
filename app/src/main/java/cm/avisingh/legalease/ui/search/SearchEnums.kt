package cm.avisingh.legalease.ui.search

enum class DocumentCategory(val displayName: String) {
    LEGAL("Legal"),
    FINANCIAL("Financial"),
    PERSONAL("Personal"),
    BUSINESS("Business"),
    PROPERTY("Property"),
    CONTRACTS("Contracts"),
    CORPORATE("Corporate"),
    OTHER("Other")
}

enum class DocumentType(val displayName: String) {
    PDF("PDF"),
    WORD("Word"),
    EXCEL("Excel"),
    IMAGE("Image"),
    TEXT("Text"),
    OTHER("Other")
}

enum class SortOption {
    RELEVANCE,
    DATE_DESC,
    DATE_ASC,
    NAME_ASC,
    NAME_DESC,
    SIZE_DESC,
    SIZE_ASC
}