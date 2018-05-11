*****************************
README for VeevaCRMOrgRefresh
*****************************

Author: Christopher Fong

**************
I - Background
**************

The script to refresh the orgs is run through a cucumber feature file under the OrgRefresh folder where all the other feature files are.
It has its own step definition and keywords file under CRM/General/OrgRefresh.  The source code of the script is under Utilities/General/OrgRefresh.
The necessary CLM presentations are under CLM_Data. If you need to add more CLM presentations, simply add them in CLM_Data
Likewise, the necessary VInsights data are under Standard-Monthly. Again if you need to add more files, add them in Standard-Monthly
DataLoaderBulkAPI.java is used when editing usernames on an org.

***********************
II - Setting up Program
***********************

There are boolean variables that you can set to tell the script what to run.

Validation variables
--------------------
approvedEmail
survey
vaultCLM
network
engage
mccpAdmin

Set them to true if you want the script to validate credentials on the apps or false otherwise.

Data variables
--------------
uploadVinsights
uploadCLMData
editUserNames

Set them to true if you want to upload the data to the orgs. False otherwise

Network configs
---------------
editNetworkUsers - edits the profiles of some users on the org
assoicateShadowOrg - associates the org with a shadow or non-shadow network

Set them to true if you want to run network configuration on the org. False otherwise

Veeva message variables
-----------------------
enterVeevaMessageFields
clearVeevaMessageFields

There are also some option variables that tells the script what kind of org it's working on

Option variables
----------------
org
isSandbox
isShadow
ftpAddress

Set the boolean variables to true if the org is a sandbox or needs shadow configuration. False otherwise

There are 2 ways to edit the variables. You can either hardcode them in the script itself or you can
do it through "debug configuration".  Whichever way you do it, MAKE SURE THE TAG IS EQUAL TO "OrgRefresh" in order to run the script.

You can edit all of the above variables through "debug configuration". Just set the boolean variables to either "true" or "false".

For example, if you want to run approved email, you would set the variable like this:
DapprovedEmail = true

For the string variables such as org and ftpAddress, you would set the variable like this:
Dorg = veeva.cucumber

The general format is D{variable name} = {value}

If there are no variables set within "debug configurations", the script will go with the values that are hardcoded in the source code. Otherwise, the
values set in "debug configuration" will override the values that are hardcoded.

The bare minimum configuration is:
test -DargLine="-DUSERLANGUAGE=en_US -Dplatform=Online  -Dbrowser=firefox " -Dcucumber.options="--tags @OrgRefresh"

Here's the full conifguration:
test -DargLine="-DUSERLANGUAGE=en_US -Dplatform=Online  -Dbrowser=firefox " -Dcucumber.options="--tags @OrgRefresh" -DapprovedEmail=false -Dsurvey=false -DvaultCLM=false -Dnetwork=false -Dengage=false -DmccpAdmin=false -DuploadVinsights=false -DuploadCLMData=false -DeditUserNames=false -DeditNetworkUsers=false -DassociateShadowOrg=false -DenterVeevaMessageFields=false -DclearVeevaMessageFields=false -Dorg=veeva.cucumber -DisSandbox=false -DisShadow=false -DftpAddress=ftp-qa.vod309.com

*************************
III - Running the Program
*************************

Edit any configurations necessary and hit debug under debug configurations. Make sure you have the bare minimum configuration stated above.

****** Reasons it could crash *******

The program can crash if it can't find a certain element on the webpage. You can check the progress of the script before it crashed by
checking the output.
