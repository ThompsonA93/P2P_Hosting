# VerSys_P2PProjekt
> 3 Subproject
> Structural P2P
> TCP-Protocl

## Message Format
@Request :: ctrl = Command (String)
		 :: information = IP@Port (String)
		 :: payload = A string of all File names. Each File name split with "\n" (String)
		 
Example:
	ctrl = "AddRessource"
	information = "127.0.0.1@3534"
	payload = "mypicture.jpg\nmytext.tex"	

@Response 	:: ctrl = "Server response" (String)
		 	:: information = Success message or not Success message (String)
		 	:: payload = 1. A string of all File names and IP@port. Each File name split with "\n". 
		 	   File name and IP@port split with "#"(String for GetRessource and ListRessources) 
		 	   besser Idee?
		 
Example:
	ctrl = "Server response"
	information = "AddRessource is successful"
	payload = "mypicture.jpg#127.0.0.1@3534\nmytext.tex#127.0.1.1@3345"

// alte version	
//@Request  :: Command 			/ Request or Offer
//	  :: [Ressource]@[Address] 	/ What is requested/offered
//Example:
//	String s = Command@picture.png@IP:Port
//@Response :: [List<String>]		/ Data returned by SuperServer 	
//Example:



	
@Command :: [Abstract Object over List of Commands]
	AddRessource();
	GetRessource();
	DetachRessouce();
	ListRessources();
	Serve();		

## SuperServer
DHT-Distributer, notwendig als /root, shutdown => Aus

DHT.list
> Key:Value(=Object)

```
-> receiveRequest(Request r);
-->> addRessource();	// Seeder meldet an
	updateDHT();	
-->> getRessource();	// Lecher Request specific file
	fetchFromDHT();
-->> detachRessource;	// Seeder meldet ab
	updateDHT();
-->> listRessources();	// Leecher request offered files
	listDHTRessouces();
```


## LeecherClient
Nur Requests/Downloads/Beziehen von Ressourcen

```
-> sendRequest(Request r);
	receive();
```

## SeederClient
Server der sich beim SuperServer anmeldet mit
* Addresse
* Ressourcen

```
-> sendRequest(Request r);
	serve();
```
