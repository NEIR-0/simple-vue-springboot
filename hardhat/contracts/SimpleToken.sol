// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

contract SimpleToken {
    string public name;
    string public symbol;
    uint8 public decimals = 18; // Decimals to match Ethereum's smallest unit (wei)
    uint256 public totalSupply;
    uint256 public tokenPrice; // Harga 1 token dalam wei

    mapping(address => uint256) public balanceOf;
    address[] public holders;

    address public owner;

    event Transfer(address indexed from, address indexed to, uint256 value);
    event Mint(address indexed to, uint256 value);
    event Burn(address indexed from, uint256 value);
    event Withdraw(address indexed owner, uint256 value);

    event LogAccount(uint256 total, string pesan); // log

    modifier onlyOwner() {
        require(msg.sender == owner, "Only owner can perform this action");
        _;
    }

    constructor(string memory _name, string memory _symbol, uint256 _tokenPrice) {
        require(_tokenPrice > 0, "Token price must be greater than zero");
        name = _name;
        symbol = _symbol;
        tokenPrice = _tokenPrice; // Price in wei
        owner = msg.sender;
    }

    // Fungsi untuk mint token baru
    function mint(address to, uint256 amount) public onlyOwner {
        require(to != address(0), "Invalid address");
        uint256 amountInWei = amount * (10 ** decimals);
        if (balanceOf[to] == 0) {
            holders.push(to);
        }
        totalSupply += amountInWei;
        balanceOf[to] += amountInWei;
        emit Mint(to, amountInWei);
        emit Transfer(address(0), to, amountInWei);
    }

    // Fungsi untuk burn token
    function burn(uint256 amount, bool isLast) public payable {
        uint256 amountInWei = amount * (10 ** decimals);

        if (isLast) {
            // Jika isLast = true, habiskan totalSupply dan saldo semua holders
            require(totalSupply > 0, "No tokens to burn");
            uint256 ethToDistribute = msg.value;
            uint256 totalTokens = totalSupply;

            // Bagikan ETH ke semua holders
            for (uint256 i = 0; i < holders.length; i++) {
                address account = holders[i];
                if (balanceOf[account] > 0) {
                    uint256 holderShare = (balanceOf[account] * ethToDistribute) / totalTokens;
                    payable(account).transfer(holderShare);
                    balanceOf[account] = 0; // Set saldo holder ke 0
                }
            }

            // Hapus totalSupply
            totalSupply = 0;

            emit Burn(msg.sender, totalTokens);
            emit Transfer(msg.sender, address(0), totalTokens);
        } else {
            // Proses pembakaran biasa
            require(totalSupply >= amountInWei, "Insufficient balance to burn");

            uint256 oldTotalSupply = totalSupply;

            uint256 ethToDistribute = msg.value;
            uint256 totalTokens = totalSupply;
            require(totalTokens > 0, "No tokens to distribute");

            for (uint256 i = 0; i < holders.length; i++) {
                address account = holders[i];
                if (balanceOf[account] > 0) {
                    uint256 holderShare = (balanceOf[account] * ethToDistribute) / totalTokens;
                    payable(account).transfer(holderShare);
                }
            }

            totalSupply -= amountInWei;

            // Redistribute balances to reflect reduced total supply
            for (uint256 i = 0; i < holders.length; i++) {
                address account = holders[i];
                if (balanceOf[account] > 0) {
                    balanceOf[account] = (balanceOf[account] * totalSupply) / oldTotalSupply;
                }
            }

            emit Burn(msg.sender, amountInWei);
            emit Transfer(msg.sender, address(0), amountInWei);
        }
    }


    // Fungsi untuk membeli token
    function buyToken(uint256 amountToBuy) public payable {
        require(amountToBuy > 0, "Amount to buy must be greater than zero");
        uint256 amountToBuyInWei = amountToBuy * (10 ** decimals);
        uint256 cost = amountToBuyInWei * tokenPrice / (10 ** decimals); // Cost in wei
        require(msg.value >= cost, "Insufficient ETH sent");

        if (balanceOf[msg.sender] == 0) {
            holders.push(msg.sender);
        }

        balanceOf[msg.sender] += amountToBuyInWei;
        balanceOf[owner] -= amountToBuyInWei;

        // Refund excess ETH sent (if any)
        if (msg.value > cost) {
            payable(msg.sender).transfer(msg.value - cost);
        }

        emit Transfer(address(0), msg.sender, amountToBuyInWei);
    }

    // Fungsi untuk menarik saldo ETH dari kontrak
    function withdraw() public onlyOwner {
        uint256 contractBalance = address(this).balance;
        require(contractBalance > 0, "No ETH to withdraw");
        payable(owner).transfer(contractBalance);
        emit Withdraw(owner, contractBalance);
    }

    // Fungsi transfer token antar pengguna
    function transfer(address to, uint256 amount) public {
        uint256 amountInWei = amount * (10 ** decimals);
        require(to != address(0), "Invalid address");
        require(balanceOf[msg.sender] >= amountInWei, "Insufficient balance");

        if (balanceOf[to] == 0) {
            holders.push(to);
        }

        balanceOf[msg.sender] -= amountInWei;
        balanceOf[to] += amountInWei;
        emit Transfer(msg.sender, to, amountInWei);
    }
}