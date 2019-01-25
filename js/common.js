const URI = "http://localhost:8080/sample/";

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
            xhttp.open("GET", URI + "inputPAN?" + "primaryAccountNumber=" + pan, true);
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
            xhttp.open("GET", URI + "inputToken?" + "token=" + token, true);
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
            xhttp.open("GET", URI + "inputRegId?" + "regId=" + regId, true);
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

        if (tokenNewlyGen !== '') {
            if (tokenNewlyGen.toLowerCase() === 'true') {
                alert("Last three but one digits of PAN should be 000");
            } else if (tokenNewlyGen.toLowerCase() === 'false') {
                alert("Last three but one digits of PAN should not be 000");
            }
        }

        if (walletType !== '') {
            switch (walletType.toLowerCase()) {
                case "android": alert("Second to last digit of reg Id should be 1 or 5 or 9"); break;
                case "apple": alert("Second to last digit of reg Id should be 2 or 6"); break;
                case "samsung": alert("Second to last digit of reg Id should be 3 or 7"); break;
            }
        }

        if (cvv !== '') {
            alert ("CVV is the last three but one digits of the token. So token value should be xxx"+cvv+"x");
        }
    });
})

function processInputRegId() {

    let regId = $("#input-regid").val();
    let xhttp = new XMLHttpRequest();
    xhttp.open("GET", URI + "inputRegId?" + "regId=" + regId, true);
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
    xhttp.open("GET", URI + "inputPAN?" + "primaryAccountNumber=" + pan, true);
    xhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            try {
                let response = JSON.parse(xhttp.responseText);
                $("#output-tokennewlygen").val(response.tokenNewlyGenerated);
                $("#output-regid").val(response.regId);
                $("#output-token").val(response.token);
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
    xhttp.open("GET", URI + "inputToken?" + "token=" + token, true);
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
    xhttp.open("GET", URI + "inputCVV?" + "cvv=" + cvv, true);
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