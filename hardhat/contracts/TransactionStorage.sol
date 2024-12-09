// SPDX-License-Identifier: UNLICENSED
pragma solidity ^0.8.28;

contract StringStorageHistory {
    // Menyimpan alamat pembuat contract
    address public owner;

    // Event untuk log transaksi dan hash-nya
    event StringStored(address indexed sender, string data, bytes32 hash);

    // Konstruktor untuk menetapkan owner saat deploy contract
    constructor() {
        owner = msg.sender;
    }

    // Fungsi untuk menerima Ether dan string dari user, mengirim Ether ke owner, dan menyimpan string
    function storeStringWithPayment(string memory data) public payable returns (bytes32) {
        // Pastikan pengirim mengirimkan Ether
        require(msg.value > 0, "You must send some Ether");

        // Hitung hash dari string yang diberikan
        bytes32 dataHash = keccak256(abi.encodePacked(data));
        
        // Emit event untuk menyimpan log
        emit StringStored(msg.sender, data, dataHash);

        // Kirimkan Ether yang diterima ke owner
        payable(owner).transfer(msg.value);

        // Kembalikan hash sebagai hasil
        return dataHash;
    }
}