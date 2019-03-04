# ObjectStore Reference Application
ObjectStore reference application is built to showcase how to develop a multi-cloud based application using a single-code line.

## Description
The basic idea behind this reference application is to build a multi-cloud application, which can be run in SAP CP consuming ObjectStore service with different IaaS providers underneath - e.g. AWS, GCP, OpenStack, Azure. The focus is particulary on single-code line thereby making the application portable across different cloud providers. To achieve this a java based open source library [JClouds](https://jclouds.apache.org/) is used which abstracts out the differences between different IaaS specific SDKs and provides simplified and portable apis to develop a multi-cloud application.

The application uses Jclouds 2.1.2. At present, the application supports objectstore service from three different iaas providers namely: AWS S3, Google Cloud Storage(GCS) and OpenStack Swift.

Following jclouds dependencies are used in the application.

~~~
    <!-- jclouds dependencies -->
    <dependency>
      <groupId>org.apache.jclouds.provider</groupId>
      <artifactId>aws-s3</artifactId>
      <version>${jclouds.version}</version>
    </dependency>
    <dependency>
      <groupId>org.apache.jclouds.provider</groupId>
      <artifactId>google-cloud-storage</artifactId>
      <version>${jclouds.version}</version>
    </dependency>
    <dependency>
      <groupId>org.apache.jclouds.api</groupId>
      <artifactId>openstack-swift</artifactId>
      <version>${jclouds.version}</version>
    </dependency>
    <dependency>
      <groupId>org.apache.jclouds.api</groupId>
      <artifactId>openstack-keystone</artifactId>
      <version>${jclouds.version}</version>
    </dependency>
~~~
The size of each of the jclouds dependencies are as follows: 
~~~
    Dependency                Size
    -------------             -------
    aws-s3                    29kb
    google-cloud-storage      158kb
    openstack-swift           137kb
    openstack-keystone        294kb
~~~

Besides spring-boot and jclouds the other dependencies used in the application are **jackson-databind**: to parse json file, **guava**: to build BlobStoreContext and to convert input stream to byte array, **commons-fileupload**: to upload files. For full information about the dependencies please refer the [pom](./pom.xml).  



#### Application Usecase

- The application creates RESTful endpoints using which you can upload, download, delete and list files.
- The application then uploads the file to the Object Storage service. Behind the scenes, JClouds library abstracts to the code to upload the different providers like AWS S3, Google Cloud Storage, OpenStack Swift. 

## Table of Contents
- [Scope of the application](#scope-of-the-application)
- [Architecture](#architecture)
- [Requirements](#requirements)
- [Download and Installation](#download-and-installation)
  - [Build the application](#build-the-application)
  - [Deploy the application on Cloud Foundry](#deploy-the-application-on-cloud-foundry)
  - [Test the application](#test-the-application)
    - [To Upload a file / object](#upload-a-file--object)
    - [To download a file / object](#download-a-file--object)
    - [To delete a file / object](#delete-a-file--object)
    - [To list all the files / objects](#list-all-the-files--objects)
- [Known Issues](#known-issues)
- [How to Obtain Support](#how-to-obtain-support)
- [TO-DO](#to-do)
- [License](#license)

## Scope of the Application
- Support for AWS S3, GCS and OpenStack Swift ObjectStore
- Upload a file / object to an ObjectStore
- Download a file / object from an ObjectStore
- Delete a file / object from an ObjectStore
- List all the files / objects in an ObjectStore

## Architecture

![Alt text](./documents/objectstore-sample-architecture.jpg "Architecture")

## Requirements
- [Java 8](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Apache Maven](https://maven.apache.org/download.cgi)
- [Cloud Foundry CLI](https://github.com/cloudfoundry/cli#downloads)
- [SAP Cloud Platform Global account](https://help.sap.com/viewer/e275296cbb1e4d5886fa38a2a2c78c06/Cloud/en-US/667f34ba9222450491c2b848cd17e189.html)
- [Provision ObjectStore service](https://help.sap.com/viewer/e275296cbb1e4d5886fa38a2a2c78c06/Cloud/en-US/667f34ba9222450491c2b848cd17e189.html)
- A Cloud Foundry user with SpaceDeveloper role to deploy the application

## Download and Installation

#### Build the Application
[Clone](https://help.github.com/articles/cloning-a-repository/) the application `objectstore-sample` to your system

Clone URL :  `https://github.wdf.sap.corp/refapps/objectstore-sample.git`
Navigate to the root folder of the application and run the below maven command to build the application:
```
mvn clean install
```

#### Deploy the Application on Cloud Foundry

  1. Login to Cloud Foundry by typing the below commands on command prompt
     ```
     cf api <api>
     cf login -u <username> -p <password> 
     ```
     `api` - [URL of the Cloud Foundry landscape](https://help.sap.com/viewer/65de2977205c403bbc107264b8eccf4b/Cloud/en-US/350356d1dc314d3199dca15bd2ab9b0e.html) that you are trying to connect to.
    
     `username` - Email address of your sap.com account.
     `password` - Your sap.com password
    
     Select the org and space when prompted to. For more information on the same refer [link](https://help.sap.com/viewer/65de2977205c403bbc107264b8eccf4b/Cloud/en-US/75125ef1e60e490e91eb58fe48c0f9e7.html#loio4ef907afb1254e8286882a2bdef0edf4).
    

  2. Create the Cloud Foundry Object Store Service Instance

     The application is designed to run on multi-cloud platforms. In the current scope the application can run on AWS, GCP and OpenStack landscapes.

     - To run the application on AWS landscape, create a service by executing the below command:

       `cf create-service objectstore s3-standard objectstore-service`

     - To run the application on GCP landscape, create a service by executing the below command:

       `cf create-service objectstore gcs-standard objectstore-service`
        
     - To run the application on OpenStack landscape, create a service by executing the below command:

       `cf create-service objectstore swift-standard objectstore-service`

      > <b>*Please don't change the service instance name i.e. `objectstore-service`*</b>

  3. Edit manifest.yml file. Replace the `<unique_id>` placeholder with your *SAP User ID* so that the host name is unique in the CF landscape. You can find your *SAP User ID* in [your sap.com profile](https://people.sap.com/#personal_info).

  ~~~
    ---
    applications:
    - name: objectstore-sample-svc
     ------------------------------------------
    | host: <unique_id>-objectstore-sample-svc |
     ------------------------------------------
      memory: 2G
      buildpack: https://github.com/cloudfoundry/java-buildpack.git
      path: target/objectstore-sample-1.0.0.jar
      services:
        - objectstore-service
  ~~~

  4. To deploy / run the application, Navigate to the root of the application execute the below command:
     ```
     cf push
     ```

#### Test the Application

[Postman Client](https://www.getpostman.com/apps) can be used to test / access the REST API endpoints.

##### Upload a file / object

<b>POST</b>

To upload a file / object set the below request body and hit the endpoint url. Changes the <application URL> with yours.

EndPoint URL :   `<application URL>/objectstorage.svc/api/v1/storage/`

Request Body : form-data with key-value pair. Pass the name of the key as `file` and the value is `the path of the file`.

> For the file upload, we have provided a [test file](/documents/test.rtf) in the documents folder which you can use if needed for the upload testing.

On a successful upload operation following would be found:

Status: 202

Response Body: `<uploaded_filename> uploaded successfully`


##### List all the files / objects

<b>GET</b>

To get the list of a files / objects set the content-type and hit the below endpoint url. Changes the <application URL> with yours.

EndPoint URL :   `https://<application URL>/objectstorage.svc/api/v1/storage/`

Content-Type : `application/json`

On a successful get operation following would be found:

Status: 200

Response Body:
~~~
[
    {
        "etag": "CIjak4uDxeACEAE=",
        "bucket": "sap-cp-osaas-a78345d3-e45d-42eb-9c03-c47393d0d436",
        "name": "SampleFile.pdf",
        "url": "https://www.googleapis.com/storage/v1/b/sap-cp-osaas-a78345d3-e45d-42eb-9c03-c47393d0d436/o/SampleFile.pdf",
        "lastModified": "Mon Feb 18 15:30:22 IST 2019",
        "size": "245.7 KB",
        "contentType": "application/pdf",
        "userMetadata": {
            "description": "sample content"
        }
    },
    {
        "etag": "COf+0p7uxOACEAE=",
        "bucket": "sap-cp-osaas-a78345d3-e45d-42eb-9c03-c47393d0d436",
        "name": "SampleImage.jpg",
        "url": "https://www.googleapis.com/storage/v1/b/sap-cp-osaas-a78345d3-e45d-42eb-9c03-c47393d0d436/o/SampleImage.jpg",
        "lastModified": "Mon Feb 18 13:57:06 IST 2019",
        "size": "46.1 KB",
        "contentType": "image/jpeg",
        "userMetadata": {
            "description": "sample content"
        }
    }
  ...

]
~~~

##### Download a file / object

<b>GET</b>

> Please open any browser and hit the below endpoint url to download a file / object rather than using Postman to test it.</b>

EndPoint URL :   `https://<application URL>/objectstorage.svc/api/v1/storage/{file-name}`

##### Delete a file / object

<b>DELETE</b>

To delete a file / object hit the below endpoint url by appending the file / object name in postman.

EndPoint URL :   `https://<application URL>/objectstorage.svc/api/v1/storage/{file-name}`

On a successful delete operation following would be found:

Status: 200

Response Body: `<file-name> deleted from ObjectStore.`

## How to Obtain Support

In case you find a bug, or you need additional support, please open an issue here in GitHub.

## Known Issues
- BlobStore removeBlob() api does not work in Jclouds OpenStack Swift and a bug is reported to jclouds team. Please click [here](https://issues.apache.org/jira/browse/JCLOUDS-1483) to know more about the issue.
- Multipart Upload feature is not supported in jclouds OpenStack Swift and a bug is reported to jclouds team. Please click [here](https://issues.apache.org/jira/browse/JCLOUDS-1482) to know more about the issue.
- Using InpuStream instead of ByteArray to upload large files to make the read operation faster causes issues. Please click [here](https://issues.apache.org/jira/browse/JCLOUDS-1451) to know more about the issue. 

## TO-DO
- To provide support for Azure ObjectStore
- To provide multipart upload support for large files
- To fix portability issues w.r.t OpenStack Swift
- To support enterprise level features e.g object-level tagging to ease search operation
- To support ACL (Access Controlled List) implementation

## License

Copyright (c) 2018 SAP SE or an SAP affiliate company. All rights reserved. This file is licensed under SAP Sample Code License Agreement, except as noted otherwise in the [LICENSE](/LICENSE) file.
