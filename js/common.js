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
            let first6 = pan.substr(0, 6);
            let last4 = pan.substr(pan.length - 4, pan.length);
            let mid = pan.substr(6, pan.length - 10);
            let regId = '999999999' - mid + '';
            let output_mid = mid.split("").reverse().join("");
            $("#input-token").val(first6 + output_mid + last4);
            $("#input-regid").val(first6.split("").reverse().join("") + regId + last4.split("").reverse().join(""));
        } else if (token !== '') {
            let first6 = token.substr(0, 6);
            let last4 = token.substr(token.length - 4, token.length);
            let mid = token.substr(6, token.length - 10);
            let regId = '999999999' - mid + '';
            let output_mid = mid.split("").reverse().join("");
            $("#input-pan").val(first6 + output_mid + last4);
            $("#input-regid").val(first6.split("").reverse().join("") + regId.split("").reverse().join("") + last4.split("").reverse().join(""));
        } else if (regId !== '') {
            let first6 = regId.substr(0, 6);
            let last4 = regId.substr(regId.length - 4, regId.length);
            let mid = regId.substr(6, regId.length - 10);
            let outputMid = '999999999' - mid + '';
            $("#input-pan").val(first6.split("").reverse().join("") + outputMid + last4.split("").reverse().join(""));
            $("#input-token").val(first6.split("").reverse().join("") + outputMid.split("").reverse().join("") + last4.split("").reverse().join(""));
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
    let first6 = regId.substr(0, 6);
    let last4 = regId.substr(regId.length - 4, regId.length);
    let mid = regId.substr(6, regId.length - 10);
    let panMid = '999999999' - mid + '';
    if (first6.substring(0, 1) == '3') {
        while (panMid.length < 5) {
            panMid = '0' + panMid;
        }
    } else {
        while (panMid.length < 6) {
            panMid = '0' + panMid;
        }
    }
    let tokenMid = panMid.split("").reverse().join("");
    let pan = first6.split("").reverse().join("") + panMid + last4.split("").reverse().join("");
    let token = first6.split("").reverse().join("") + tokenMid + last4.split("").reverse().join("");
    $("#output-expdate").val("5001");
    $("#output-pan").val(pan);
    $("#output-token").val(token);
    let secondToLastDigit = regId.substr(regId.length - 2, 1);
    secondToLastDigit = secondToLastDigit % 4 + '';
    let walletType = '', eci = '';
    switch (secondToLastDigit) {
        case "1": walletType = "ANDROID"; eci = "07"; break;
        case "2": walletType = "APPLE"; eci = "05"; break;
        case "3": walletType = "SAMSUNG"; eci = "07"; break;

    }
    $("#output-wallet").val(walletType);
    $("#output-eci").val(eci);
}

function processInputPan () {

    let pan = $("#input-pan").val();
    let first6 = pan.substr(0, 6);
    let last4 = pan.substr(pan.length - 4, pan.length);
    let mid = pan.substr(6, pan.length - 10);
    let regId = '999999999' - mid;
    let output_mid = mid.split("").reverse().join("");
    let isTokenNew = pan.substr(pan.length-4, 3);
    $("#output-token").val(first6 + output_mid + last4);
    if (isTokenNew == '000') {
        $("#output-tokennewlygen").val("true");
    } else {
        $("#output-tokennewlygen").val("false");
    }
    $("#output-regid").val(first6.split("").reverse().join("") + regId + last4.split("").reverse().join(""));

}

function processInputToken () {
    let token = $("#input-token").val();
    let first6 = token.substr(0, 6);
    let last4 = token.substr(token.length - 4, token.length);
    let mid = token.substr(6, token.length - 10);
    mid = mid.split("").reverse().join("");
    let cvv = token.substr(token.length-4, 3);
    $("#output-pan").val(first6 + mid + last4);
    if (token.length > 3) {
        let regStart = token.substr(0, 6);
        regStart = regStart.split("").reverse().join("");
        let regMid = token.substr(6, token.length - 4);
        regMid = regMid.split("").reverse().join("");
        let regEnd = token.substr(token.length - 4, token.length);
        regEnd = regEnd.split("").reverse().join("");
        $("#output-regid").val(regStart + regMid + regEnd);
    } else {
        $("#output-regid").val(token);
    }
    $("#output-cvv").val(cvv);
    $("#output-expdate").val("5001");

}

function processInputCvv () {
    let cvv = $("#input-cvv").val();
    let orderLvt = '3';
    while (orderLvt.length < 18) {
        if (cvv !== '') {
            orderLvt += cvv;
        } else {
            orderLvt += '00000000000000000';
        }
    }
    orderLvt = orderLvt.substr(0,18);
    $("#output-orderlvt").val(orderLvt);
}