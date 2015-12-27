var itemsA = '{"items":['
    +'{"itemName":"GoldStar","quantity":"1" },'
    +'{"itemName":"cola","quantity":"2" },'
    +'{"itemName":"Heineken","quantity":"3" }]}';


var billObj2 = new Object();
billObj2.bill_id ="123";
billObj2.fb_id ="456";
billObj2.isPrivate = "true";
billObj2.items =  JSON.parse(itemsA);


$(function() {
    $("#sendBill").click(function(){
        $.ajax({
            url:"http://localhost:8080/rest/bill/check",
            type: 'post',
            data:JSON.stringify(billObj2),
            method: 'json',
            success: function(response) {
                if(response){
                    document.getElementById("succ").innerHTML = response;
                }
            },
            error: function(r){
                consule.log(r);
            }
        });
    });
});