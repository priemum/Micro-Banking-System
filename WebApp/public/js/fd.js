const addFD = async (data) => {
  try {
    console.log(data);
    const res = await axios({
      method: "POST",
      url: "/api/v1/fd/",
      data: data,
    });

    if (res.data.status === "success") {
      alert("Successfully Added FD");
      window.setTimeout(() => {
        location.assign(`/account-overview?id=${data.customerID}`);
      }, 1500);
    }
  } catch (err) {
    alert(err.response.data.message);
    //showAlert("error", err.response.data.message);
  }
};

const withdrawFD = async (data) => {
  try {
    console.log(data);
    const res = await axios({
      method: "POST",
      url: "/api/v1/fd/withdraw",
      data: data,
    });

    if (res.data.status === "success") {
      // showAlert("success", "Logged in successfully!");
      alert("Successfully Withdrawn FD");
      window.setTimeout(() => {
        location.assign(`/`);
      }, 1500);
    }
  } catch (err) {
    alert(err.response.data.message);
    //showAlert("error", err.response.data.message);
  }
};

document.querySelector(".form--withdraw").addEventListener("submit", (e) => {
  e.preventDefault();
  const FD_ID = document.getElementById("FD_ID").value;
  const data = {
    FD_ID: FD_ID,
  };
  console.log("withdraw method");
  withdrawFD(data);
});

document.querySelector(".form--fd").addEventListener("submit", (e) => {
  alert("Adding");
  e.preventDefault();

  const accountNumber = document.getElementById("accountNumber").value;
  const customerID = document.getElementById("customerID").value;
  const amount = document.getElementById("amount").value;
  this.dateTime = new Date();
  this.openingDate = this.dateTime.toISOString().slice(0, 19).replace("T", " ");
  const planType = document.getElementById("planType").value;
  const data = {
    accountNumber: accountNumber,
    customerID: customerID,
    amount: amount,
    openingDate: openingDate,
    closingDate: openingDate,
    planType: planType,
  };
  console.log(data);
  addFD(data);
});