$(document).ready(function () {
    $("#input-btn").click(function () {
        let pan = $("#input-pan").val();
        let token = $("#input-token").val();
        let regId = $("#input-regid").val();
        let cvv = $("#input-cvv").val();
        if (pan !== '') {
            processInputPan();
        } else if (token !== '') {
            processInputToken();
        } else if (regId !== '') {
            processInputRegId();
        } else if (cvv !== '') {
            processInputCvv();
        }
    });

    $("#input-reset-btn").click(function () {
        $("input[id^=input]").val('');
    });

    $("#output-reset-btn").click(function () {
        $("input[id^=output]").val('');
        $("td[id*=output-token-]").html("");
        $("select[id^=output]").html("");
    });
    
    $("#output-btn").click(function () {
        let pan = $("#output-pan").val();
        let token = $("#output-token").val();
        let regId = $("#output-regid").val();
        let tokenNewlyGen = $("#output-tokennewlygen").val();
        let walletType = $("#output-wallet").val();
        let cvv = $("#output-cvv").val();

        if (pan !== '') {
            let xhttp = new XMLHttpRequest();
            xhttp.open("GET",  "inputPAN?" + "primaryAccountNumber=" + pan, true);
            xhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
            xhttp.onreadystatechange = function() {
                if (this.readyState == 4 && this.status == 200) {
                    try {
                        let response = JSON.parse(xhttp.responseText);
                        $("#input-regid").val(response.regId);
                        $("#input-token").val(response.token);
                    } catch {
                        alert("EWS API not return correct response");
                    }
                }
            }
            xhttp.send(null);

        } else if (token !== '') {
            let xhttp = new XMLHttpRequest();
            xhttp.open("GET", "inputToken?" + "token=" + token, true);
            xhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
            xhttp.onreadystatechange = function() {
                if (this.readyState == 4 && this.status == 200) {
                    try {
                        let response = JSON.parse(xhttp.responseText);
                        $("#input-regid").val(response.regId);
                        $("#input-pan").val(response.pan);
                    } catch {
                        alert("EWS API not return correct response");
                    }
                }
            }
            xhttp.send(null);

        } else if (regId !== '') {
            let xhttp = new XMLHttpRequest();
            xhttp.open("GET", "inputRegId?" + "regId=" + regId, true);
            xhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
            xhttp.onreadystatechange = function() {
                if (this.readyState == 4 && this.status == 200) {
                    try {
                        let response = JSON.parse(xhttp.responseText);
                        $("#input-pan").val(response.pan);
                        $("#input-token").val(response.token);
                    } catch {
                        alert("EWS API not return correct response");
                    }
                }
            }
            xhttp.send(null);

        }

        let alertMsg = '';
        if (tokenNewlyGen !== '') {
            if (tokenNewlyGen.toLowerCase() === 'true') {
                alertMsg += "Token Newly Generated - true: Last three but one digits of PAN should be 0X0, where X is any integer\n";
            } else if (tokenNewlyGen.toLowerCase() === 'false') {
                alertMsg += "Token Newly Generated - false: Last three but one digits of PAN should not be 0X0, where X is any integer\n";
            }
        }

        if (walletType !== '') {
            switch (walletType.toLowerCase()) {
                case "android": alertMsg += "Wallet Type - Android: Second to last digit of RegId should be 1\n"; break;
                case "apple": alertMsg += "Wallet Type - Apple: Second to last digit of RegId should be 2\n"; break;
                case "samsung": alertMsg += "Wallet Type - Samsung: Second to last digit of RegId should be 3\n"; break;
            }
        }

        if (cvv !== '') {
            alertMsg += "CVV - " + cvv + ": Should be the last three but one digits of the token. So token value should be xxx"+cvv+"x\n";
        }

        if (alertMsg != '') {
            alert(alertMsg);
        }
    });
})

function processInputRegId() {

    let regId = $("#input-regid").val();
    let xhttp = new XMLHttpRequest();
    xhttp.open("GET", "inputRegId?" + "regId=" + regId, true);
    xhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            try {
                let response = JSON.parse(xhttp.responseText);
                $("#output-wallet").val(response.walletType);
                $("#output-eci").val(response.eci);
                $("#output-expdate").val(response.expDate);
                $("#output-pan").val(response.pan);
                $("#output-token").val(response.token);
                $("#output-token-pan-first6-last4-mod10").html(response.tokenWithPANFirst6Last4Mod10);
                $("#output-token-pan-first6-last4-mod11").html(response.tokenWithPANFirst6Last4Mod11);
                $("#output-token-random-mod10").html(response.tokenRandomMod10);
                $("#output-token-random-mod11").html(response.tokenRandomMod11);
                $("#output-token-pan-last4-mod10").html(response.tokenWithPANLast4Mod10);
                $("#output-token-pan-last4-mod11").html(response.tokenWithPANLast4Mod11);
                $("#output-token-vault1-mod10").html(response.tokenVault1Mod10);
                $("#output-token-vault1-mod11").html(response.tokenVault1Mod11);
            } catch {
                alert("EWS API not return correct response");
            }
        }
    }

    xhttp.send(null);
}

function processInputPan () {

    let pan = $("#input-pan").val();
    let xhttp = new XMLHttpRequest();
    xhttp.open("GET", "inputPAN?" + "primaryAccountNumber=" + pan, true);
    xhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            try {
                let response = JSON.parse(xhttp.responseText);
                console.log(response.tokenNewlyGenerated);
                $("#output-tokennewlygen").val(response.tokenNewlyGenerated.toString());
                $("#output-regid").val(response.regId);
                $("#output-token").val(response.token);
                $("#output-token-pan-first6-last4-mod10").html(response.tokenWithPANFirst6Last4Mod10);
                $("#output-token-pan-first6-last4-mod11").html(response.tokenWithPANFirst6Last4Mod11);
                $("#output-token-random-mod10").html(response.tokenRandomMod10);
                $("#output-token-random-mod11").html(response.tokenRandomMod11);
                $("#output-token-pan-last4-mod10").html(response.tokenWithPANLast4Mod10);
                $("#output-token-pan-last4-mod11").html(response.tokenWithPANLast4Mod11);
                $("#output-token-vault1-mod10").html(response.tokenVault1Mod10);
                $("#output-token-vault1-mod11").html(response.tokenVault1Mod11);
            } catch {
                alert("EWS API not return correct response");
            }
        }
    }

    xhttp.send(null);
}

function processInputToken () {

    let token = $("#input-token").val();
    let xhttp = new XMLHttpRequest();
    xhttp.open("GET", "inputToken?" + "token=" + token, true);
    xhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            try {
                let response = JSON.parse(xhttp.responseText);
                $("#output-cvv").val(response.cvv);
                $("#output-expdate").val(response.expDate);
                $("#output-regid").val(response.regId);
                $("#output-pan").val(response.pan);
            } catch {
                alert("EWS API not return correct response");
            }
        }
    }

    xhttp.send(null);
}

function processInputCvv () {

    let cvv = $("#input-cvv").val();
    console.log(cvv);
    let xhttp = new XMLHttpRequest();
    xhttp.open("GET", "inputCVV?" + "cvv=" + cvv, true);
    xhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            try {
                let response = JSON.parse(xhttp.responseText);
                $("#output-orderlvt").val(response.orderLVT);
            } catch {
                alert("EWS API not return correct response");
            }
        }
    }

    xhttp.send(null);
}