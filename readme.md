# S3 File Management API
This is a Spring Boot-based API for managing files stored in an Amazon S3 bucket. The application allows users to search for files, upload new files, and retrieve download links for files stored in their specific user folders on S3.

## Features
* Search Files: Search for files in a user's folder by a search term.
* Upload Files: Upload files to a user's folder.
* Download Files: Download files from a user's folder by their key.

## Technologies
* Spring Boot: For building the RESTful API.
* Amazon S3 SDK: To interact with Amazon S3 for file storage.
* JUnit 5 & Mockito: For unit and integration tests.


## Endpoints
1. Search Files:
   * URL: /api/s3/search
   * Method: GET
   * Query Parameters:
       * userName (String): The name of the user whose folder you want to search in.
        * q (String): The search term to filter file names by.
   * Response: A list of file information (fileKey and downloadUrl).
  #### Example Request:
  ``  GET /api/s3/search?userName=testUser&q=document  ``
   
  #### Example Response:
   ````
   
   [
   {
   "fileKey": "testUser/document1.pdf",
   "downloadUrl": "https://mock-url.com/document1.pdf"
   },
   {
   "fileKey": "testUser/document2.txt",
   "downloadUrl": "https://mock-url.com/document2.txt"
   }
   ]
   ````
2. Upload File
   * URL: /api/s3/upload/{userName}
   * Method: POST
   * Path Variables:
       *userName (String): The name of the user who owns the folder to which the file will be uploaded.
   *  Request Body: A multipart/form-data request containing the file.
   * Response: A success message upon successful upload.
   #### Example Request:
 
   `` POST /api/s3/upload/testUser
       Content-Type: multipart/form-data``
  #### Example Response:
  ````
   {
   "message": "File uploaded successfully"
   }
   ````
   ## Setup & Installation
   ### Prerequisites
*    Java 21 
*    Maven (or Gradle) for dependency management
*    AWS S3 credentials configured (via environment variables or application.properties)
  
 ### Clone the repository
   ```` git clone https://github.com/rinshadkv/s3-service.git ````

   ``cd s3-service ``
   ### Configure AWS Credentials
  configure the aws credentials  using the application.properties file.

#### properties

```commandline
aws.s3.bucket-name=your-s3-bucket-name
aws.accessKeyId=your-aws-access-key
aws.secretAccessKey=your-aws-secret-key
aws.s3.region=ap-south-1
```
### Build and Run the Application
```commandline
./gradlew bootRun
```

### Run Tests
To run the unit tests:
```commandline
./gradlew test
```

### API Documentation
This API uses Postman for documentation. After running the application, you can use the postman collection provided with this repo:


 