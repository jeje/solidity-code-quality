# Scanner for Solidity vulnerabilities or bad practices

This tool analyzes Solidity contracts for known vulnerabilities or bad practices.

This is still a work in progress though.
In the end it will be provided as both a CLI tool and a Sonar plugin for CI pipelines.

### Analysis

#### Vulnerabilities

* [tx.origin](http://solidity.readthedocs.io/en/latest/security-considerations.html#tx-origin)

    You'll get an ```CRITICAL``` error if you compare ```tx.origin``` to ```msg.sender``` has this is usually not a
    safe thing to do!
    
    This analysis is smart enough to figure out such a comparison even if ```tx.origin``` and/or ```msg.sender```
    are assigned to variables.
    
#### Bad Practices

* [Throw deprecated](https://media.consensys.net/when-to-use-revert-assert-and-require-in-solidity-61fb2c0e5a57)

  ```throw``` should not be used anymore. Prefer instead ```require()``` or ```revert()``` or ```assert()```.