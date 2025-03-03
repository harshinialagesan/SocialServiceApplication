# Social Sharing Application

## Introduction
The **Social Sharing Application** is a platform that allows users to create, share, like, and comment on posts. Users can sign up using email authentication and reset their passwords if needed. The application leverages **Java Spring Boot** for backend development, **MySQL** for database management, **AWS S3** for image storage, and **Microsoft Email SSO** for secure authentication.

## Features

### User Authentication & Security
- **Sign Up with Email**: Users can register using their email and receive an SMTP notification upon successful registration.
- **Forgot/Reset Password**: Users can reset their password via email authentication if they forget their login credentials.
- **Microsoft Email SSO**: Users can log in using Microsoft’s single sign-on service for added security and convenience.

### Post Management
- **Create Posts**: Users can create posts by adding a title, description, relevant tags, and images.
- **Image Upload**: Images are stored securely on **AWS S3** with a maximum file size of **5MB**. Supported formats: **.jpg, .jpeg, .png**.

### Social Interactions
- **Commenting**: Users can leave comments on other users' posts.
- **Liking**: Users can like posts to show appreciation.
- **Sharing**: Users can share posts with others.

## Technologies Used
- **Spring Boot**: Backend framework for building a scalable API.
- **MySQL**: Relational database to store user, post, and comment data.
- **AWS S3**: Cloud storage for handling image uploads.
- **Microsoft Email SSO**: Secure authentication system.
- **SMTP Service**: Sends email notifications for signup and password reset.
- **JPA Repository**: Simplifies database interactions.
- **Lombok**: Reduces boilerplate code.
- **Spring Web**: Enables RESTful API development.
- **Maven**: Dependency management tool.

## Setup Instructions
To set up the **Social Sharing Application** on your local environment, follow these steps:

1. **Clone the Repository**
   ```bash
   git clone https://github.com/your-repo/social-sharing-app.git
   cd social-sharing-app
   ```

2. **Configure the Database**
   - Create a new MySQL database and user.
   - Update the database configuration in `application.properties`:
     ```properties
     spring.datasource.url=jdbc:mysql://localhost:3306/social_sharing_db
     spring.datasource.username=root
     spring.datasource.password=yourpassword
     ```

3. **Setup AWS S3 for Image Upload**
   - Configure S3 bucket credentials in `application.properties`:
     ```properties
     aws.s3.bucket-name=your-bucket-name
     aws.access-key=your-access-key
     aws.secret-key=your-secret-key
     ```

4. **Build the Project**
   ```bash
   mvn clean install
   ```

5. **Run the Application**
   ```bash
   mvn spring-boot:run
   ```

6. **Access the Application**
   - Open your browser and go to: `http://localhost:8080`
   - Use tools like **Postman** to test API endpoints.

## Future Enhancements
- **Real-time Notifications**: Notify users when someone likes, comments, or shares their post.
- **User Profile Management**: Allow users to customize their profiles with additional details.
- **Hashtags & Trending Posts**: Implement a trending algorithm based on likes and shares.
- **Private Messaging**: Enable users to send direct messages to each other.

This project is designed to be **scalable**, **secure**, and **user-friendly** for an engaging social media experience.

