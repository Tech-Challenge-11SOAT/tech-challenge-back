output "db_endpoint" {
  value       = module.rds.db_endpoint
  description = "The endpoint of the RDS instance"

}

output "ec2_instance_ip" {
  value       = module.ec2.ec2_instance_ip
  description = "The public IP address of the EC2 instance"

}