const { buildModule } = require("@nomicfoundation/hardhat-ignition/modules");

module.exports = buildModule("SimpleSendEtherTestSepolia", (m) => {
  const sendEther = m.contract("SimpleSendEtherTestSepolia"); // name contract

  return { sendEther };
});