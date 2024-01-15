variable "vpc_security_group_ids" {
    type = list(string)
}

variable "subnet_id" {
    type = string
}

variable "ec2_instance_key" {
    type = string
}

terraform {
  backend "s3" {}
}

provider "aws" {
    region = "eu-west-1"
}

resource "aws_instance" "mayura_terraform_test" {
    ami = "ami-0694d931cee176e7d"
    vpc_security_group_ids = var.vpc_security_group_ids
    subnet_id = var.subnet_id
    instance_type = "t2.small"   
    availability_zone = "eu-west-1c"
    key_name = var.ec2_instance_key
    associate_public_ip_address = true
    tags = {
        Name = "ubuntu-instance"
    }
}

