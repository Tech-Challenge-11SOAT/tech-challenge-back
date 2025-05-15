variable "vpc_id" {
  description = "VPC ID where the security group will be created"
  type        = string

}

variable "app_sg_id" {
  description = "App security group ID to allow MySQL traffic"
  type        = string

}