# AWS Deployment Blueprint

This file describes a deployment path; it does not mean the application has already been deployed.

1. Create ECR repositories for `enterpriseflow-api`, `enterpriseflow-analytics`, and `enterpriseflow-web`.
2. Build and push versioned images from CI.
3. Provision RDS PostgreSQL in private subnets with automated backups and encryption.
4. Provision ElastiCache Redis only if caching/session/rate-limiting features are enabled.
5. Run Java and Python services on ECS Fargate behind an Application Load Balancer.
6. Host the frontend on S3 + CloudFront, or run the frontend container behind the ALB.
7. Store DB credentials and JWT secret in Secrets Manager; inject them at runtime.
8. Configure TLS, security groups, least-privilege IAM roles, health checks, log retention, alarms, and backup restore tests.
9. Run Flyway migrations as a controlled deployment step.
10. Add blue/green or rolling deployments and rollback procedures.
