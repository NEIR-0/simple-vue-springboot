const { buildModule } = require("@nomicfoundation/hardhat-ignition/modules");

module.exports = buildModule("SimpleToken", (m) => {
  // Define the arguments for the constructor
  const name = "MyToken";        // Replace with dynamic input if needed
  const symbol = "MTK";          // Replace with dynamic input if needed
  const tokenPrice = 100000000000000;          // Replace with dynamic input if needed

  // Deploy the contract with the provided constructor arguments
  const simpleToken = m.contract("SimpleToken", [
    name,        // Token Name
    symbol,      // Token Symbol
    tokenPrice   // Token Price (in wei, as needed by the contract)
  ]);

  return { simpleToken };
});