//------------------------------------------------------------------------------
// Craete a public key
// Philip R Brenan at gmail dot com, Appa Apps Ltd, 2018
//------------------------------------------------------------------------------
package com.appaapps;
import java.security.*;
import javax.crypto.*;

public class Test
 {public static void main(String[] args)
   {final String string = "Hello World";
    say       ("Sign     : ", string);
    try
     {SecureRandom     random = SecureRandom.getInstance("SHA1PRNG");
      KeyPairGenerator keyGen =  KeyPairGenerator.getInstance("RSA");           // Key type by provider
      keyGen.initialize(1024, random);

      KeyPair    pair = keyGen.generateKeyPair();                               // Generate key pair
      PrivateKey priv = pair.getPrivate();
      PublicKey  pub  = pair.getPublic();

      Signature cdsa = Signature.getInstance("MD5withRSA");                     // Sign
      cdsa.initSign(priv);
      cdsa.update(string.getBytes());

      byte[] csig = cdsa.sign();
      sayBytes("Signed   : ", csig);

      Signature vdsa = Signature.getInstance("MD5withRSA");                     // Confirm signature
      vdsa.initVerify(pub);
      vdsa.update(string.getBytes());
      say     ("Verified : ", vdsa.verify(csig));

      Cipher cipher = Cipher.getInstance("RSA");                                // Encrypt some text
      cipher.init(Cipher.ENCRYPT_MODE, priv);
      byte[] encrypted = cipher.doFinal(string.getBytes());
      sayBytes("Encrypted: ", csig);

      cipher.init(Cipher.DECRYPT_MODE, pub);                                    // Decrypt
      byte[]decrypted = cipher.doFinal(encrypted);
      say     ("Decrypted: ", new String(decrypted));
     }
    catch(Exception e) {say(e); e.printStackTrace();}
   }

  private static void say(Object...O)
   {final StringBuilder s = new StringBuilder();
    for(Object o: O) s.append(o);
    System.err.println(s.toString());
   }

  private static void sayBytes(String title, byte[]B)
   {final StringBuilder s = new StringBuilder();
    for(byte b: B) {s.append(" "); s.append(b);}
    say(title, s.toString());
   }
 }
