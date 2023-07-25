package com.digiventure.fido_exploration.util

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Binder
import android.util.Base64
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.cert.CertificateEncodingException
import java.security.cert.CertificateException
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate

fun getApkKeyHash(aContext: Context): String? {
    val uid = Binder.getCallingUid()
    val packageNames: Array<String> = aContext.packageManager.getPackagesForUid(uid) ?: return null

    try {
        val info: PackageInfo =
            aContext.packageManager.getPackageInfo(packageNames[0], PackageManager.GET_SIGNATURES)
        val cert = info.signatures[0].toByteArray()
        val input: InputStream = ByteArrayInputStream(cert)
        val cf = CertificateFactory.getInstance("X509")
        val c: X509Certificate = cf.generateCertificate(input) as X509Certificate
        val md = MessageDigest.getInstance("SHA256")

        return "android:apk-key-hash:" + Base64.encodeToString(
            md.digest(c.encoded), Base64.DEFAULT or Base64.NO_WRAP or Base64.NO_PADDING
        ).replace('+', '-').replace('/', '_').replace("=", "")
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    } catch (e: CertificateException) {
        e.printStackTrace()
    } catch (e: NoSuchAlgorithmException) {
        e.printStackTrace()
    } catch (e: CertificateEncodingException) {
        e.printStackTrace()
    }

    return null
}