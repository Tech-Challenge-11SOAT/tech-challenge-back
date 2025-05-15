data "aws_ami" "amzn-linux-2023-ami" {
  most_recent = true
  owners      = ["amazon"]

  filter {
    name   = "name"
    values = ["al2023-ami-2023.*-x86_64"]
  }
}

resource "aws_key_pair" "tech_ssh_key" {
  key_name   = "tech-ssh-key"
  public_key = var.public_key
}

resource "aws_instance" "tech_app" {
  ami                         = data.aws_ami.amzn-linux-2023-ami.id
  instance_type               = "t3.small"
  key_name                    = aws_key_pair.tech_ssh_key.key_name
  subnet_id                   = var.app_subnet_id
  vpc_security_group_ids      = [var.app_sg_id]
  associate_public_ip_address = true
  user_data                   = file("${path.module}/userdata.sh")


  tags = {
    Name = "tech-app-instance"
  }

  lifecycle {
    create_before_destroy = true
  }
}