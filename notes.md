Upload Service
=================
Input
 - File

Output
 - File id
 - Request id

Type: API

Endpoint Details
- POST /upload
  - Request Body: File, requestId (optional)
  - Response: { "fileId": "string", "requestId": "string" }
  - If requestId is not provided, a new requestId will be generated, otherwise the provided requestId will be used.

- GET /syncResponse
    - Request Parameters: requestId
    - Response: Summarizer Response

DB details(MongoDB)
- UploadServiceDetails
    - fileId: String (Primary Key)
    - requestId: String
    - fileName: String
    - fileType: String
    - uploadTime: Timestamp
    - fileLocation: String

Whitelist Service
=================
Input
- File Location
- File id
- Request id

Output
- Per file whitelist details
 - File id
 - whitelisted
 - failure comments
 - detected type, e.g: Form 16

Type: Queue Consumer

DB details(MongoDB)
- WhitelistServiceDetails
    - fileId: String (Primary Key)
    - requestId: String
    - whitelisted: Boolean
    - failureComments: String
    - detectedType: String

Knowledge Matcher Service
=================
Input
- File id
- Request id

Output
- Per file knowledge match details
 - File id
 - Request id
 - matched items
  - matched items details

Type: Queue Consumer

DB details (Neo4j)
- KnowledgeMatcherServiceDetails
    - fileId: String (Primary Key)
    - requestId: String
    - matchedItems: {
        itemId: String,
        itemName: String,
        itemType: String,
        itemDetails: String
    }[]

Analysis Service
=================
Input
- File id
- Request id
- Matched items

Output
- Categorized analysis
 - Category
  - Category details

Type: Queue Consumer

DB details (MongoDB)
- AnalysisServiceDetails
    - requestId: String (Primary Key)
    - categoryWiseAnalysis: {
        categoryName: String,
        categoryDetails: String
    }[]

Summarizer Service
=================
Input
- Request id

Output
- Consolidated summary

Type: Queue Consumer

DB details (MongoDB)
- SummarizerServiceDetails
    - requestId: String (Primary Key)
    - summary: String