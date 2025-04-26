provider "aws" {
  region = var.aws_region
}

terraform {
  required_version = "~> 1.11.4"
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
}

module "vpc" {
  source = "./modules/vpc"

  vpc_cidr             = var.vpc_cidr
  availability_zones   = var.availability_zones
  project_name         = var.project_name
}

module "iam" {
  source = "./modules/iam"

  project_name = var.project_name
}

module "secrets" {
  source = "./modules/secrets"

  project_name  = var.project_name
  db_user       = var.db_user
  db_password   = var.db_password
  db_host       = var.db_host
  db_port       = var.db_port
  db_name       = var.db_name
  db_ssl_mode   = var.db_ssl_mode
}

module "ecs" {
  source = "./modules/ecs"

  project_name         = var.project_name
  vpc_id               = module.vpc.vpc_id
  private_subnets      = module.vpc.private_subnets
  ecs_task_execution_role_arn = module.iam.ecs_task_execution_role_arn
  ecs_task_role_arn    = module.iam.ecs_task_role_arn
  container_image      = var.container_image
  container_port       = var.container_port
  cpu                  = var.cpu
  memory               = var.memory
  desired_count        = var.desired_count
  secrets_manager_arn  = module.secrets.secrets_manager_arn
}

module "alb" {
  source = "./modules/alb"

  project_name    = var.project_name
  vpc_id          = module.vpc.vpc_id
  private_subnets  = module.vpc.private_subnets
  security_groups = [module.api_gateway.api_gateway_security_group_id]
}

module "api_gateway" {
  source = "./modules/api-gateway"

  project_name      = var.project_name
  vpc_id            = module.vpc.vpc_id
  alb_dns_name      = module.alb.alb_dns_name
  alb_listener_arn  = module.alb.alb_listener_arn
  private_subnets   = module.vpc.private_subnets
  api_endpoints     = var.api_endpoints
  target_group_arn  = module.ecs.target_group_arn
}
