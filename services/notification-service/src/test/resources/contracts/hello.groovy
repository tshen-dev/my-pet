package contracts

import org.springframework.cloud.contract.spec.Contract
Contract.make {
  description "should return even when number input is even"
  request{
    method POST()
    url("/notifications")
  }
  response {
    status 204
  }
}