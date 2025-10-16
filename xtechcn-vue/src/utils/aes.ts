import CryptoJS from "crypto-js";

const VUE_APP_PWD_ENC_KEY = "xtechcn-xtechcn-xtechcn-xtechcn-,xtechcn-xtechcn-";

/**
 *加密处理
 */
export function encryption(src: string) {
  const key = CryptoJS.enc.Utf8.parse(VUE_APP_PWD_ENC_KEY.split(",")[0]);
  const iv = CryptoJS.enc.Utf8.parse(VUE_APP_PWD_ENC_KEY.split(",")[1]);
  // 加密
  const encrypted = CryptoJS.AES.encrypt(src, key, {
    iv,
    mode: CryptoJS.mode.CBC,
    padding: CryptoJS.pad.Iso10126
  });
  return encrypted.toString();
}
