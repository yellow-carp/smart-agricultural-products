import { generateMD5Hash } from "@/utils/utils";
/**
 * 随机生成大小写字符串
 * @param length 字符串长度
 * @returns {string}
 */
const randomCaseString = (length: number, hasNum: boolean) => {
  let result = "";
  const characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
  const charactersNum = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
  const used = hasNum ? charactersNum : characters;
  const charactersLength = used.length;
  for (let i = 0; i < length; i++) {
    const randomIndex = Math.floor(Math.random() * charactersLength);
    const randomCharacter = used.charAt(randomIndex);
    result += randomCharacter;

    // 10% chance to toggle case
    if (Math.random() < 0.1) {
      result =
        result.slice(0, i) +
        (result[i].toUpperCase() === result[i]
          ? result[i].toLowerCase()
          : result[i].toUpperCase()) +
        result.slice(i + 1);
    }
  }
  return result;
};

/**
 * 生成签名
 * @returns {*}
 */
export const initSignature = (url: string) => {
  if (!url.startsWith("/")) {
    url = "/" + url;
  }
  // 去除空格问题
  if (url.endsWith(" ")) {
    url = url.split(" ").join("");
  }
  // 去除参数
  if (url.includes("?")) {
    url = url.split("?")[0];
  }
  // // 黑名单
  // const blackList = ['']
  // if (blackList.some((item) => url.includes(item))) return
  // 混入8个单位长度字符串
  const mixin = randomCaseString(8, true);
  // 常规字段
  const nonce = randomCaseString(16, false);
  const salt = randomCaseString(8, true);
  const timestamp = new Date().getTime();
  // 生成签名
  const md5key = generateMD5Hash(
    import.meta.env.VITE_APP_FIXED + nonce + url + timestamp + salt,
    32
  );
  // 混入
  const signatureFront = md5key.slice(0, 8);
  const signatureAfter = md5key.slice(8, 32);
  const signature = signatureFront + mixin + signatureAfter + randomCaseString(3, true);
  return {
    nonce,
    salt,
    timestamp,
    url,
    signature
  };
};
