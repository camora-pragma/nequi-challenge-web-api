variable "aws_region" {
  description = "AWS Region for the infrastructure"
  type        = string
  default     = "us-east-1"
}

variable "project_name" {
  description = "Name of the project"
  type        = string
  default     = "nequi-challenge"
}

variable "vpc_cidr" {
  description = "CIDR block for the VPC"
  type        = string
  default     = "10.0.0.0/16"
}

variable "availability_zones" {
  description = "Availability Zones"
  type        = list(string)
  default     = ["us-east-1a", "us-east-1b", "us-east-1c"]
}

variable "container_image" {
  description = "ECR Image URL"
  type        = string
  default     = "237577132646.dkr.ecr.us-east-1.amazonaws.com/nequi-challenge:latest"
}

variable "container_port" {
  description = "Port exposed by the container"
  type        = number
  default     = 8080
}

variable "cpu" {
  description = "CPU units for the task"
  type        = number
  default     = 256
}

variable "memory" {
  description = "Memory for the task in MiB"
  type        = number
  default     = 512
}

variable "desired_count" {
  description = "Desired number of tasks"
  type        = number
  default     = 1
}

variable "api_endpoints" {
  description = "API endpoints to be configured in API Gateway"
  type        = list(string)
  default     = [
    "POST /api/v1/franchise",
    "POST /api/v1/franchise/{franchiseId}/branch",
    "GET /api/v1/franchise/{franchiseId}/branches/products/max-stock",
    "POST /api/v1/branch/{branchId}/product",
    "DELETE /api/v1/branch/{branchId}/product/{productId}",
    "PUT /api/v1/product/{productId}"
  ]
}

variable "db_user" {
  description = "Database username"
  type        = string
  sensitive   = true
  default     = "admin"
}

variable "db_password" {
  description = "Database password"
  type        = string
  sensitive   = true
  default     = ""
}

variable "db_host" {
  description = "Database host"
  type        = string
  default     = "localhost"
}

variable "db_port" {
  description = "Database port"
  type        = string
  default     = "5432"
}

variable "db_name" {
  description = "Database name"
  type        = string
  default     = "mydb"
}

variable "db_ssl_mode" {
  description = "Database SSL mode"
  type        = string
  default     = "disable"
}