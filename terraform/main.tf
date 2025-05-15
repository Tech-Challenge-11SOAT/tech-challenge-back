terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }

  backend "s3" {
    bucket       = "tech-challenge-soat11-terraform-state"
    key          = "terraform/state"
    region       = "us-east-1"
    use_lockfile = true
  }

}

provider "aws" {
  region = "us-east-1"

}

module "ec2" {
  source        = "./modules/ec2"
  public_key    = var.public_key
  app_subnet_id = module.vpc.app_subnet_id
  app_sg_id     = module.sg_app.app_sg_id
}

module "rds" {
  source         = "./modules/rds"
  db_username    = var.db_username
  db_password    = var.db_password
  db_subnet_id_1 = module.vpc.db_subnet_id_1
  db_subnet_id_2 = module.vpc.db_subnet_id_2
  db_sg_id       = module.sg_db.db_sg_id
}

module "vpc" {
  source = "./modules/vpc"

}

module "sg_app" {
  source = "./modules/security_group/app"
  vpc_id = module.vpc.vpc_id
}

module "sg_db" {
  source    = "./modules/security_group/db"
  vpc_id    = module.vpc.vpc_id
  app_sg_id = module.sg_app.app_sg_id

}