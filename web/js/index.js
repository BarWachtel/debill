
var bill1;
var user1;
var billSummary1;
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


    function user(i_id,i_firstName, i_lastName, i_facebookId) {
        this.id = i_id;
        this.firstName = i_firstName;
        this.lastName = i_lastName;
        this.facebookId = i_facebookId;
       // this.creationTime = i_creationTime;
    }

    var usersArray = new Array();
    user1 = new user(1,"Dima","Poleacov","123");
    var user2 = new user(1,"Bar","Wachtel","1234");
    var user3 = new user(1,"Yotam","Lende","12345");

    usersArray.push(user1);
    usersArray.push(user2);
    usersArray.push(user3);


    function bill(i_id,i_manager, i_isPrivate, i_isOpen, i_items) {
        this.id = i_id;
        this.manager = i_manager;
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


    function itemSummary(i_user,i_item,i_paid_For)
    {
        this.user = i_user;
        this.item = i_item;
        this.paidFor = i_paid_For;
    }

    var itemSummary1 = new itemSummary(user1,item1,1);
    var itemSummary2 = new itemSummary(user2,item2,1);
    var itemSummary3 = new itemSummary(user1,item2,1);
    var itemSummary4 = new itemSummary(user3,item3,1);

    var itemsSummaryArray = new Array();
    itemsSummaryArray.push(itemSummary1);
    itemsSummaryArray.push(itemSummary2);
    itemsSummaryArray.push(itemSummary3);
    itemsSummaryArray.push(itemSummary4);

    function billSummary(i_bill,i_itemSummary)
    {
        this.bill = i_bill;
        this.itemSummary = i_itemSummary;
    }

    billSummary1 = new billSummary(bill1,itemsSummaryArray);


    document.getElementById("date_demo").innerHTML = JSON.stringify(date);
    document.getElementById("users_demo").innerHTML = JSON.stringify(usersArray);
    document.getElementById("items_demo").innerHTML = JSON.stringify(itemsArray);
    document.getElementById("bill_demo").innerHTML = JSON.stringify(bill1);
    document.getElementById("group_demo").innerHTML = JSON.stringify(group1);
    document.getElementById("manager_demo").innerHTML = JSON.stringify(user1);
    document.getElementById("itemSummary_demo").innerHTML = JSON.stringify(itemSummary1);
    document.getElementById("billSummary_demo").innerHTML = JSON.stringify(billSummary1);
});

$(function() {
    $("#sendBill").click(function(){
        $.ajax({
            url:"http://localhost:8080/rest/bill/add",
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
            url:"http://localhost:8080/rest/user/user",
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