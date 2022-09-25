// pass req.body.data to the constructor
class Customer {
  constructor(data) {
    this.tableName = "customer";
    this.data = data;

    this.customerID = Math.floor(Math.random() * 10000001);
    this.password = data.password; // required
    this.confirmPass = data.confirmPass; // required
    this.customerNIC = data.customerNIC; // required
    this.agentID = data.agentID; // required
    this.firstName = data.firstName; // required
    this.lastName = data.lastName; // required
    this.contactNumber = data.contactNumber; // required
    this.address = data.address; // required
    this.birthday = new Date(data.birthday); // required
    this.statement = this.generateInsertStatement();
  }

  generateInsertStatement() {
    const cols =
      "(`customerID`, `password`, `customerNIC`, `firstName`, `lastName`, `contactNumber`, `address`, `birthday`, `agentID`)";
    var statement = `INSERT INTO ${this.tableName} ${cols} VALUES`;
    var values = `('${this.customerID}','${this.password}', '${this.customerNIC}', '${this.firstName}', '${this.lastName}', '${this.contactNumber}', '${this.address}', '${this.birthday}', '${this.agentID}')`;
    return `${statement} ${values}`;
  }
}

module.exports = Customer;
