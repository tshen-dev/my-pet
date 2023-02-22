package contracts.notifications

import org.springframework.cloud.contract.spec.Contract

Contract.make {
  description "should return 200 when request dto have .to not contains 500"
  request {
    method POST()
    url("/notifications")
    headers {
      contentType(applicationJson())
    }
    body(
        to: $(consumer(regex('^((?!500).)*$')), producer("tshen.petproject@gmail.com"))
    )
  }
  response {
    headers {
      contentType(applicationJson())
    }
    status 200
    body(
        message: "SUCCESS"
    )
  }
}
