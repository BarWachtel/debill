~~Setup git~~<br/>
Singelton MySQL db connection -

##API (Rest (javax.ws.rs.*) OR Servlets (HttpServlets))
####Only non-authorized paths -
	get /view/ - index (screen)
	get /facebook - setup user session

####Authorized paths (redirect to index for non-authed users) - <br/>
	get /view/main - open bill, join bill
		get /view/openbill - camera interaction etc.
			post /bill/create - user needs to upload photo (content-type: image/jpg), returns bill id
			get /bill/{id}/items - returns all items
			get /bill/{id}/users - users connected to bill
		get /view/joinbill - show all available open bills
			get /bill/ - get all bills (those that belong to friends)
		get /view/bill - displays items & payment status
			post /bill/{id}/item/{id} - param. quantity
			post /bill/{id}/close - Only the user who opened the bill
		get /view/billsummary - who payed what

And more...

##Logic & Redis -


##Database -<br/>
User (DAO)<br/>
Bill (DAO)<br/>
Item (DAO)<br/>
User_to_Bill (link table)<br/>
Bill_to_Item (link table)<br/>
User_to_Item (link table)<br/>

DAO - Data Access Object

How to add readme.md markdown - <br/>
https://github.com/adam-p/markdown-here/wiki/Markdown-Cheatsheet