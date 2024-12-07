// SPDX-License-Identifier: UNLICENSED
pragma solidity ^0.8.28;

contract SimpleSendEther {
    // Fungsi untuk mengirim Ether ke alamat lain
    function sendEther(address payable _recipient) public payable {
        require(msg.value > 0, "You need to send some Ether"); // Memastikan nilai yang dikirim lebih dari 0
        require(_recipient != address(0), "Invalid recipient address"); // Memastikan alamat penerima valid

        // Transfer Ether ke alamat penerima
        (bool success, ) = _recipient.call{value: msg.value}("");
        require(success, "Failed to send Ether");
    }

    // Fungsi fallback untuk menerima Ether
    receive() external payable {}
}