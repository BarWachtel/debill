
var bill1;
var user1;
$(function(){


    function date(i_day, i_month,i_year) {
        this.day = i_day;
        this.month = i_month;
        this.year = i_year;

    }
    var date = new date(5,1,2016);
    var itemsArray = new Array();

    function item(i_name, i_quantity, i_price, i_bill_id,i_num_paid) {
        this.name = i_name;
        this.quantity = i_quantity;
        this.price = i_price;
        this.billId = i_bill_id;
        this.numPaidFor = i_num_paid;
    }

    var item1 = new item("Cola",1,10,1,0);
    var item2 = new item("GoldStar",2,25,1,0);
    var item3 = new item("Heineken",1,27,1,0);

    itemsArray.push(item1);
    itemsArray.push(item2);
    itemsArray.push(item3);


    function user(i_id,i_firstName, i_lastName, i_facebookId, i_creationTime) {
        this.id = i_id;
        this.firstName = i_firstName;
        this.lastName = i_lastName;
        this.facebookId = i_facebookId;
        this.creationTime = i_creationTime;
    }

    var usersArray = new Array();
    user1 = new user(1,"Dima","Poleacov","123",date);
    var user2 = new user(1,"Bar","Wachtel","1234",date);
    var user3 = new user(1,"Yotam","Lende","12345",date);

    usersArray.push(user1);
    usersArray.push(user2);
    usersArray.push(user3);


    function bill(i_id,i_manager, i_isPrivate, i_isOpen, i_items) {
        this.id = i_id;
        this.User = i_manager;
        this.isPrivate = i_isPrivate;
        this.isOpen =i_isOpen;
        this.items = i_items;
    }

    bill1= new bill(1,user1,"false","true",itemsArray);

    function group(i_users,i_bill)
    {
        this.users = i_users;
        this.bill = i_bill;
    }

    var group1 = new group(usersArray,bill1);

    document.getElementById("date_demo").innerHTML = JSON.stringify(date);
    document.getElementById("users_demo").innerHTML = JSON.stringify(usersArray);
    document.getElementById("items_demo").innerHTML = JSON.stringify(itemsArray);
    document.getElementById("bill_demo").innerHTML = JSON.stringify(bill1);
    document.getElementById("group_demo").innerHTML = JSON.stringify(group1);
    document.getElementById("manager_demo").innerHTML = JSON.stringify(user1);
});

$(function() {
    $("#sendBill").click(function(){
        $.ajax({
            url:"http://localhost:8080/rest/bill/addNewBill",
            method: "POST",
            data:JSON.stringify(bill1),
            dataType:'json',
            contentType:"application/json",
            success: function(jsonResponse) {
                console.log(jsonResponse);
                var res = JSON.stringify(jsonResponse);
                console.log(jsonResponse);
                console.log(res);
                document.getElementById("success").innerHTML = "bill was send successufly: " + res;
            },
            error: function(r){
                consule.log(r);
            }
        });
    });
});

$(function() {
    $("#isertManager").click(function(){
        $.ajax({
            url:"http://localhost:8080/rest/user/addBillManager",
            method: "POST",
            data:JSON.stringify(user1),
            dataType:'json',
            contentType:"application/json",
            success: function(jsonResponse) {
                console.log(jsonResponse);
                var res = JSON.stringify(jsonResponse);
                console.log(res);
                document.getElementById("success").innerHTML = "manager was insert successufly: " + res;
            },
            error: function(r){
                consule.log(r);
            }
        });
    });
});

$(function() {
    $("#getAllBills").click(function(){
        $.ajax({
            url:"http://localhost:8080/rest/bill/",
            method: "GET",
            dataType:'json',
            success: function(jsonResponse) {
                console.log(jsonResponse);
                var res = JSON.stringify(jsonResponse);
                console.log(res);
                document.getElementById("success").innerHTML = "All bills: " + res;
            },
            error: function(r){
                consule.log(r);
            }
        });
    });
});