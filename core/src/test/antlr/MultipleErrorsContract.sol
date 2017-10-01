pragma solidity ^0.4.0;


contract ThrowDeprecationContract {
    address owner;

    function ThrowDeprecationContract() {
        owner = msg.sender;
    }

    function useSuperPowers(){
        // do something only the owner should be allowed to do
        if (msg.sender != owner) {
            throw;
        }
    }

    function transferTo(address dest, uint amount) {
        require(tx.origin == owner);
        dest.transfer(amount);
    }

}
