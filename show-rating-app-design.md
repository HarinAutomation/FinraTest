# Show Rating App - Design Document

## 1. Project Overview

**App Name:** Show Rating App  
**Purpose:** Allow users to browse shows and rate them (1-5 stars)  
**Platform:** Cross-platform mobile app (iOS/Android)  
**Target Users:** General audience who watch TV shows/movies  

## 2. Technical Architecture

### 2.1 Tech Stack

**Frontend:**
- Flutter (Dart language)
- iOS and Android from single codebase

**Backend:**
- Python FastAPI
- RESTful API architecture

**Database:**
- MongoDB Atlas (NoSQL)
- 512MB free tier

**Authentication:**
- AWS Cognito User Pool
- JWT token-based auth
- 50K users free tier

**File Storage:**
- Cloudflare R2 (show images)
- 10GB free tier

**Infrastructure:**
- AWS EC2 t2.micro (12 months free)
- VPC with public/private subnets
- Terraform for Infrastructure as Code

**CI/CD:**
- GitHub Actions (2000 minutes/month free)
- Private GitHub repository

**Monitoring:**
- Sentry (application errors, 5K/month free)
- AWS CloudWatch (server metrics, free tier)

**CDN & Security:**
- Cloudflare (CDN, DNS, SSL, DDoS protection)
- Free tier

### 2.2 Infrastructure Diagram

```
Flutter App → Cloudflare → AWS VPC → External Services
     │            │           │            │
     │            │           │            ├─ MongoDB Atlas
     │            │           │            ├─ Sentry
     │            │           │            └─ IAM
     │            │           │
     │            │           ├─ Public Subnet (EC2)
     │            │           └─ Private Subnet (CloudWatch)
     │            │
     │            ├─ DNS Resolution
     │            ├─ CDN (Global Edge)
     │            ├─ R2 Image Storage
     │            └─ API Proxy
     │
     └─ Mobile App (iOS/Android)
```

### 2.3 Cost Structure

**Year 1 (Free Tier):**
- Total: $0/month

**Year 2+ (Post Free Tier):**
- AWS EC2: $8.50/month
- All other services: Free
- **Total: $8.50/month**

## 3. Application Features

### 3.1 Core Features (MVP)

**EPIC 1: Show Discovery & Rating**

*Feature 1.1: Show Catalog*
- Browse available shows
- Search shows by title
- View show thumbnails
- Filter by genre

*Feature 1.2: Show Details*
- View show information (title, description, cast)
- See average rating and rating count
- View show poster/images

*Feature 1.3: Rating System*
- Rate shows (1-5 stars)
- View personal rating history
- Update existing ratings
- See community average ratings

### 3.2 User Stories

**Core User Stories:**
- As a user, I want to browse shows so I can discover content
- As a user, I want to rate shows so I can share my opinion
- As a user, I want to see average ratings so I know what others think
- As a user, I want to search shows so I can find specific content

**Technical Stories:**
- As a system, I need secure authentication so users can have personal ratings
- As a system, I need image storage so show posters load quickly
- As a system, I need API rate limiting so the service stays stable

## 4. Database Design

### 4.1 MongoDB Collections

**Shows Collection:**
```javascript
{
  "_id": ObjectId,
  "title": "Breaking Bad",
  "description": "High school chemistry teacher turns to cooking meth",
  "genre": ["Drama", "Crime", "Thriller"],
  "image_url": "https://r2.yourapp.com/images/breaking-bad.jpg",
  "cast": ["Bryan Cranston", "Aaron Paul"],
  "release_year": 2008,
  "average_rating": 4.8,
  "rating_count": 1250,
  "created_at": ISODate,
  "updated_at": ISODate
}
```

**Ratings Collection:**
```javascript
{
  "_id": ObjectId,
  "user_id": "cognito-user-123",
  "show_id": ObjectId("show-id"),
  "rating": 5,
  "created_at": ISODate,
  "updated_at": ISODate
}
```

**Users Collection (Optional - Cognito handles auth):**
```javascript
{
  "_id": ObjectId,
  "cognito_user_id": "cognito-user-123",
  "username": "moviefan2024",
  "total_ratings": 45,
  "created_at": ISODate
}
```

## 5. API Design

### 5.1 Authentication Endpoints

```
POST /auth/login
POST /auth/register
POST /auth/refresh
POST /auth/logout
```

### 5.2 Shows Endpoints

```
GET    /api/shows              # List all shows (paginated)
GET    /api/shows/{id}         # Get show details
GET    /api/shows/search?q=    # Search shows
GET    /api/shows?genre=       # Filter by genre
```

### 5.3 Ratings Endpoints

```
POST   /api/shows/{id}/ratings # Rate a show
GET    /api/shows/{id}/ratings # Get show ratings
PUT    /api/ratings/{id}       # Update user's rating
DELETE /api/ratings/{id}       # Delete user's rating
GET    /api/users/me/ratings   # Get user's all ratings
```

### 5.4 API Response Format

**Success Response:**
```json
{
  "success": true,
  "data": {
    "shows": [...],
    "pagination": {
      "page": 1,
      "limit": 20,
      "total": 150
    }
  }
}
```

**Error Response:**
```json
{
  "success": false,
  "error": {
    "code": "VALIDATION_ERROR",
    "message": "Rating must be between 1 and 5",
    "details": {}
  }
}
```

## 6. Security Considerations

### 6.1 Authentication & Authorization
- AWS Cognito for user management
- JWT tokens for API authentication
- Token refresh mechanism
- Rate limiting per user/IP

### 6.2 Data Protection
- HTTPS everywhere (Cloudflare SSL)
- Input validation and sanitization
- SQL injection prevention (NoSQL)
- CORS configuration

### 6.3 Infrastructure Security
- VPC with private subnets
- Security groups (firewall rules)
- IAM roles with least privilege
- Cloudflare DDoS protection

## 7. Performance Optimization

### 7.1 Caching Strategy
- Cloudflare CDN for images
- API response caching (future: Redis)
- Database indexing on frequently queried fields

### 7.2 Database Optimization
- Indexes on: show_id, user_id, title, genre
- Aggregation pipelines for rating calculations
- Connection pooling

### 7.3 Mobile App Optimization
- Image lazy loading
- Pagination for show lists
- Offline capability (cached data)

## 8. Deployment Strategy

### 8.1 Infrastructure as Code
- Terraform for AWS resources
- Version controlled infrastructure
- Environment separation (staging/production)

### 8.2 CI/CD Pipeline

**GitHub Actions Workflow:**
1. Code push to main branch
2. Run automated tests
3. Build Docker image
4. Deploy to staging
5. Run integration tests
6. Deploy to production
7. Health checks

### 8.3 Monitoring & Alerting
- Sentry for application errors
- CloudWatch for infrastructure metrics
- Custom dashboards for key metrics
- Alert on high error rates or downtime

## 9. Future Enhancements

### 9.1 Phase 2 Features
- User profiles and social features
- Watchlists and favorites
- Show recommendations
- Comments and reviews
- Push notifications

### 9.2 Technical Improvements
- Redis caching layer
- Elasticsearch for advanced search
- Load balancer for high availability
- Multi-region deployment
- Advanced rating algorithms

### 9.3 Scaling Considerations
- Auto-scaling EC2 instances
- Database sharding
- CDN optimization
- Microservices architecture

## 10. Development Timeline

### 10.1 Phase 1 (MVP) - 4 weeks
- Week 1: Infrastructure setup (Terraform, AWS, Cloudflare)
- Week 2: Backend API development (FastAPI, MongoDB)
- Week 3: Flutter app development (UI, API integration)
- Week 4: Testing, deployment, bug fixes

### 10.2 Phase 2 (Enhanced Features) - 6 weeks
- Advanced features implementation
- Performance optimization
- User testing and feedback
- Production hardening

## 11. Risk Assessment

### 11.1 Technical Risks
- **AWS free tier expiration:** Mitigated by low ongoing costs
- **API rate limits:** Mitigated by caching and optimization
- **Database scaling:** MongoDB Atlas handles automatic scaling

### 11.2 Business Risks
- **User adoption:** Mitigated by MVP approach and user feedback
- **Content licensing:** Using public domain or user-generated content
- **Competition:** Focus on unique features and user experience

## 12. Success Metrics

### 12.1 Technical Metrics
- API response time < 200ms
- App crash rate < 1%
- 99.9% uptime
- Error rate < 0.1%

### 12.2 Business Metrics
- User registration rate
- Daily/Monthly active users
- Average ratings per user
- User retention rate

---

**Document Version:** 1.0  
**Last Updated:** January 2024  
**Next Review:** February 2024