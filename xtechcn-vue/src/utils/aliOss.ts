import { usePoliciesStore } from "@/store/policeies";
import OSS from "ali-oss";

const policesStore = usePoliciesStore();

let client = new OSS({
  // yourRegion填写Bucket所在地域。以华东1（杭州）为例，yourRegion填写为oss-cn-hangzhou。
  region: "oss-cn-hangzhou",
  // 从STS服务获取的临时访问密钥（AccessKey ID和AccessKey Secret）。
  // accessKeyId: policesStore.policiesInfo.accessKey,
  accessKeyId: "123123",
  accessKeySecret: policesStore.policiesInfo.secretKey,
  // 从STS服务获取的安全令牌（SecurityToken）。
  stsToken: policesStore.policiesInfo.sessionToken,
  // 填写Bucket名称。
  bucket: "single-syj"
});

export const ossUpload: any = async (file: any) => {
  const uid = new Date().getTime();
  const moment = new Date();
  const date = `${moment.getFullYear()}/${moment.getMonth() + 1}/${moment.getDate()}`;

  const fileName = `/pic/${date}/${uid}.${file.name.split(".")[1]}`;

  const options = {
    headers: { "Content-Type": "image/png" }
  };

  try {
    const res = await client.put(fileName, file, options);

    return {
      url: import.meta.env.VITE_ALIYUN_OSS_BUCKET + res.name,
      domain: import.meta.env.VITE_ALIYUN_OSS_BUCKET,
      originalName: file.name,
      objectName: res.name,
      channel: "aliyun",
      fileType: 1
    };
  } catch (e) {
    await policesStore.syncGetPolicyInfo();
    client = new OSS({
      // yourRegion填写Bucket所在地域。以华东1（杭州）为例，yourRegion填写为oss-cn-hangzhou。
      region: "oss-cn-hangzhou",
      // 从STS服务获取的临时访问密钥（AccessKey ID和AccessKey Secret）。
      accessKeyId: policesStore.policiesInfo.accessKey,
      accessKeySecret: policesStore.policiesInfo.secretKey,
      // 从STS服务获取的安全令牌（SecurityToken）。
      stsToken: policesStore.policiesInfo.sessionToken,
      // 填写Bucket名称。
      bucket: "single-syj"
    });

    return await ossUpload(file);
  }
};
