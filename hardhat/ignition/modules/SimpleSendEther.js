const { buildModule } = require("@nomicfoundation/hardhat-ignition/modules");

module.exports = buildModule("SimpleSendEther", (m) => {
  const sendEther = m.contract("SimpleSendEther"); // name contract

  return { sendEther };
});