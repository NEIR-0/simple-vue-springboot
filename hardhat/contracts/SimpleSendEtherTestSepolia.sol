// SPDX-License-Identifier: UNLICENSED
pragma solidity ^0.8.28;

contract SimpleSendEtherTestSepolia {
    function sendSepoliaTestnet(address payable _recipient) public payable {
        require(msg.value > 0, "You need to send some Ether");
        require(_recipient != address(0), "Invalid recipient address");

        (bool success, ) = _recipient.call{value: msg.value}("");
        require(success, "Failed to send Ether");
    }

    receive() external payable {}
}