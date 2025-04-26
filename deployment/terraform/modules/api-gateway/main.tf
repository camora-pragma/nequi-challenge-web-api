resource "aws_security_group" "api_gateway" {
  name        = "${var.project_name}-api-gw-sg"
  description = "Security group for the API Gateway VPC Links"
  vpc_id      = var.vpc_id

  egress {
    from_port       = 0
    to_port         = 0
    protocol        = "-1"
    cidr_blocks     = ["0.0.0.0/0"]
  }
}

resource "aws_apigatewayv2_vpc_link" "main" {
  name               = "${var.project_name}-vpc-link"
  security_group_ids = [aws_security_group.api_gateway.id]
  subnet_ids         = var.private_subnets
}

resource "aws_apigatewayv2_api" "main" {
  name          = "${var.project_name}-api"
  protocol_type = "HTTP"
}

resource "aws_apigatewayv2_integration" "alb" {
  api_id             = aws_apigatewayv2_api.main.id
  integration_type   = "HTTP_PROXY"
  integration_method = "ANY"
  integration_uri    = var.alb_listener_arn
  connection_type    = "VPC_LINK"
  connection_id      = aws_apigatewayv2_vpc_link.main.id
}

resource "aws_apigatewayv2_route" "main" {
  count     = length(var.api_endpoints)
  api_id    = aws_apigatewayv2_api.main.id

  route_key = var.api_endpoints[count.index]

  target    = "integrations/${aws_apigatewayv2_integration.alb.id}"
}

resource "aws_apigatewayv2_stage" "main" {
  api_id      = aws_apigatewayv2_api.main.id
  name        = "$default"
  auto_deploy = true
}
resource "aws_lb_listener_rule" "api_paths" {
  count        = length(var.api_endpoints)
  listener_arn = var.alb_listener_arn
  priority     = 100 + count.index

  condition {
    path_pattern {
      values = [
        replace(
          replace(
            replace(
              split(" ", var.api_endpoints[count.index])[1],
              "{franchiseId}", "*"
            ),
            "{branchId}", "*"
          ),
          "{productId}", "*"
        )
      ]
    }
  }

  action {
    type             = "forward"
    target_group_arn = var.target_group_arn
  }
}