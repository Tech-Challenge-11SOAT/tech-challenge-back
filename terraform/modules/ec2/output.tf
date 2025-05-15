output "ec2_instance_ip" {
  value = aws_instance.tech_app.public_ip

}