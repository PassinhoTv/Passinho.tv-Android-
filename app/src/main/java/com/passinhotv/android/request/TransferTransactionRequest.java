package com.passinhotv.android.request;

import android.util.Log;

import com.google.common.base.Charsets;
import com.google.common.primitives.Bytes;
import com.google.common.primitives.Longs;
import com.passinhotv.android.crypto.Base58;
import com.passinhotv.android.crypto.CryptoProvider;
import com.passinhotv.android.util.SignUtil;
import com.wavesplatform.wavesj.PrivateKeyAccount;

public class TransferTransactionRequest {
    public static int SignatureLength = 64;
    public static int KeyLength = 32;
    public static int MaxAttachmentSize = 140;
    public static int MinFee = 100000;

    public String assetId;
    public String senderPublicKey;
    public String recipient;
    public long amount;
    public long timestamp;
    public String feeAssetId;
    public long fee;
    public String attachment;
    public String signature;

    public transient final int txType = 4;
    public TransferTransactionRequest(String assetId, String senderPublicKey, String recipient, long amount,
                                      long timestamp, long fee, String attachment) {
        this.assetId = assetId;
        this.feeAssetId = assetId;
        this.senderPublicKey = senderPublicKey;
        this.recipient = recipient;
        this.amount = amount;
        this.timestamp = timestamp;
        this.fee = fee;
        this.attachment = attachment != null ? Base58.encode(attachment.getBytes(Charsets.UTF_8)) : null;
    }

    public byte[] toSignBytes() {
        try {
            byte[] timestampBytes  = Longs.toByteArray(timestamp);
            byte[] assetIdBytes = SignUtil.arrayOption(assetId);
            byte[] amountBytes     = Longs.toByteArray(amount);
            byte[] feeAssetIdBytes = SignUtil.arrayOption(feeAssetId);
            byte[] feeBytes        = Longs.toByteArray(fee);

            return Bytes.concat(new byte[] {txType},
                    Base58.decode(senderPublicKey),
                    assetIdBytes,
                    feeAssetIdBytes,
                    timestampBytes,
                    amountBytes,
                    feeBytes,
                    Base58.decode(recipient),
                    SignUtil.arrayWithSize(attachment));
        } catch (Exception e) {
            Log.e("Wallet", "Couldn't create seed", e);
            return new byte[0];
        }
    }

    public void sign(byte[] privateKey)  {
        if (signature == null) {
            signature = Base58.encode(CryptoProvider.sign(privateKey, toSignBytes()));
        }
    }
    public String getSignature(){
        return signature;
    }
    public int getAttachmentSize() {
        if (attachment == null) {
            return 0;
        }
        try {
            return Base58.decode(attachment).length;
        } catch (Base58.InvalidBase58 invalidBase58) {
            invalidBase58.printStackTrace();
            return 0;
        }
    }

//    public TransferTransaction createDisplayTransaction() {
//        TransferTransaction tt = new TransferTransaction(4, Base58.encode(Hash.fastHash(toSignBytes())),
//                AddressUtil.addressFromPublicKey(senderPublicKey), timestamp, amount, fee,
//                assetId, recipient, attachment);
//        tt.isPending = true;
//        return tt;
//    }
}
