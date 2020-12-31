export default {
  strtr: function (str, dic) {
    const makeToken = (inx) => `{{###~${inx}~###}}`;

    const tokens = Object.keys(dic)
      .map((key, inx) => ({
        key,
        val: dic[key],
        token: makeToken(inx)
      }));

    const tokenizedStr = tokens.reduce((carry, entry) =>
      carry.replace(entry.key, entry.token), str);

    return tokens.reduce((carry, entry) =>
      carry.replace(entry.token, entry.val), tokenizedStr);
  }
}
