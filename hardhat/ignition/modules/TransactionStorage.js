const { buildModule } = require("@nomicfoundation/hardhat-ignition/modules");

module.exports = buildModule("StringStorageHistory", (m) => {
  const TransactionHistory = m.contract("StringStorageHistory"); // name contract

  return { TransactionHistory };
});