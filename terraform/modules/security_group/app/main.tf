resource "aws_security_group" "tech_app_sg" {
  name        = "app_sg"
  description = "Allow SSH and HTTP traffic"
  vpc_id      = var.vpc_id

}

resource "aws_vpc_security_group_ingress_rule" "app_allow_ssh" {
  security_group_id = aws_security_group.tech_app_sg.id
  cidr_ipv4         = "0.0.0.0/0"
  from_port         = 22
  to_port           = 22
  ip_protocol       = "tcp"
}

resource "aws_vpc_security_group_ingress_rule" "app_allow_http" {
  security_group_id = aws_security_group.tech_app_sg.id
  cidr_ipv4         = "0.0.0.0/0"
  from_port         = 8080
  to_port           = 8080
  ip_protocol       = "tcp"
}


resource "aws_vpc_security_group_egress_rule" "allow_all_egress_ipv4" {
  security_group_id = aws_security_group.tech_app_sg.id
  cidr_ipv4         = "0.0.0.0/0"
  ip_protocol       = "-1"
}

resource "aws_vpc_security_group_egress_rule" "allow_all_egress_ipv6" {
  security_group_id = aws_security_group.tech_app_sg.id
  cidr_ipv6         = "::/0"
  ip_protocol       = "-1"
}

