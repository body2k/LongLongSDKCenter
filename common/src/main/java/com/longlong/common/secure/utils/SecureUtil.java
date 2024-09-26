
package com.longlong.common.secure.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.SneakyThrows;
import com.longlong.common.secure.CloudUser;
import com.longlong.common.secure.TokenInfo;
import com.longlong.common.secure.constant.SecureConstant;
import com.longlong.common.secure.exception.SecureException;
import com.longlong.common.secure.provider.IClientDetails;
import com.longlong.common.secure.provider.IClientDetailsService;
import com.longlong.common.utils.*;
import com.longlong.constant.JwtConstant;

import javax.crypto.spec.SecretKeySpec;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.*;

/**
 * Secure工具类
 *
 * 
 */
public class SecureUtil {
	private static final String USER_REQUEST_ATTR = "_USER_REQUEST_ATTR_";

	private final static String HEADER = JwtConstant.HEADER;
	private final static String BEARER = JwtConstant.BEARER;
	private final static String ACCOUNT = JwtConstant.ACCOUNT;
	private final static String USER_ID = JwtConstant.USER_ID;
	private final static String USER_CODE = JwtConstant.USER_CODE;
	private final static String ROLE_ID = JwtConstant.ROLE_ID;
	private final static String USER_NAME = JwtConstant.USER_NAME;
	private final static String ROLE_NAME = JwtConstant.ROLE_NAME;
	private final static String TENANT_ID = JwtConstant.TENANT_ID;
	private final static String CLIENT_ID = JwtConstant.CLIENT_ID;
	private final static Integer AUTH_LENGTH = JwtConstant.AUTH_LENGTH;
	private static String BASE64_SECURITY = Base64.getEncoder().encodeToString(JwtConstant.SIGN_KEY.getBytes(Charsets.UTF_8));

	private static IClientDetailsService clientDetailsService;

	static {
		clientDetailsService = SpringUtil.getBean(IClientDetailsService.class);
	}

	/**
	 * 获取用户信息
	 *
	 * @return longUser
	 */
	public static CloudUser getUser() {
		HttpServletRequest request = WebUtil.getRequest();
		if (request == null) {
			return null;
		}
		// 优先从 request 中获取
		Object longUser = request.getAttribute(USER_REQUEST_ATTR);
		if (longUser == null) {
			longUser = getUser(request);
			if (longUser != null) {
				// 设置到 request 中
				request.setAttribute(USER_REQUEST_ATTR, longUser);
			}
		}
		return (CloudUser) longUser;
	}

	/**
	 * 获取用户信息
	 *
	 * @param request request
	 * @return longUser
	 */
	public static CloudUser getUser(HttpServletRequest request) {
		Claims claims = getClaims(request);
		if (claims == null) {
			return null;
		}
		String clientId = Func.toStr(claims.get(SecureUtil.CLIENT_ID));
		Integer userId = Func.toInt(claims.get(SecureUtil.USER_ID));
		String userCode = Func.toStr(claims.get(SecureUtil.USER_CODE));
		String tenantId = Func.toStr(claims.get(SecureUtil.TENANT_ID));
		String roleId = Func.toStr(claims.get(SecureUtil.ROLE_ID));
		String account = Func.toStr(claims.get(SecureUtil.ACCOUNT));
		String roleName = Func.toStr(claims.get(SecureUtil.ROLE_NAME));
		String userName = Func.toStr(claims.get(SecureUtil.USER_NAME));
		CloudUser haUser = new CloudUser();
		haUser.setClientId(clientId);
		haUser.setUserId(userId);
		haUser.setUserCode(userCode);
		haUser.setTenantId(tenantId);
		haUser.setAccount(account);
		haUser.setRoleId(roleId);
		haUser.setRoleName(roleName);
		haUser.setUserName(userName);
		return haUser;
	}


	/**
	 * 获取用户id
	 *
	 * @return userId
	 */
	public static Integer getUserId() {
		CloudUser user = getUser();
		return (null == user) ? -1 : user.getUserId();
	}

	/**
	 * 获取用户id
	 *
	 * @return userCode
	 */
	public static String getUserCode() {
		CloudUser user = getUser();
		return (null == user) ? "" : user.getUserCode();
	}
	/**
	 * 获取用户id
	 *
	 * @param request request
	 * @return userId
	 */
	public static Integer getUserId(HttpServletRequest request) {
		CloudUser user = getUser(request);
		return (null == user) ? -1 : user.getUserId();
	}

	/**
	 * 获取用户账号
	 *
	 * @return userAccount
	 */
	public static String getUserAccount() {
		CloudUser user = getUser();
		return (null == user) ? StringPool.EMPTY : user.getAccount();
	}

	/**
	 * 获取用户账号
	 *
	 * @param request request
	 * @return userAccount
	 */
	public static String getUserAccount(HttpServletRequest request) {
		CloudUser user = getUser(request);
		return (null == user) ? StringPool.EMPTY : user.getAccount();
	}

	/**
	 * 获取用户名
	 *
	 * @return userName
	 */
	public static String getUserName() {
		CloudUser user = getUser();
		return (null == user) ? StringPool.EMPTY : user.getUserName();
	}

	/**
	 * 获取用户名
	 *
	 * @param request request
	 * @return userName
	 */
	public static String getUserName(HttpServletRequest request) {
		CloudUser user = getUser(request);
		return (null == user) ? StringPool.EMPTY : user.getUserName();
	}

	/**
	 * 获取用户角色
	 *
	 * @return userName
	 */
	public static String getUserRole() {
		CloudUser user = getUser();
		return (null == user) ? StringPool.EMPTY : user.getRoleName();
	}

	/**
	 * 获取用角色
	 *
	 * @param request request
	 * @return userName
	 */
	public static String getUserRole(HttpServletRequest request) {
		CloudUser user = getUser(request);
		return (null == user) ? StringPool.EMPTY : user.getRoleName();
	}

	/**
	 * 获取租户ID
	 *
	 * @return tenantId
	 */
	public static String getTenantId() {
		CloudUser user = getUser();
		return (null == user) ? StringPool.EMPTY : user.getTenantId();
	}

	/**
	 * 获取租户ID
	 *
	 * @param request request
	 * @return tenantId
	 */
	public static String getTenantId(HttpServletRequest request) {
		CloudUser user = getUser(request);
		return (null == user) ? StringPool.EMPTY : user.getTenantId();
	}

	/**
	 * 获取客户端id
	 *
	 * @return tenantId
	 */
	public static String getClientId() {
		CloudUser user = getUser();
		return (null == user) ? StringPool.EMPTY : user.getClientId();
	}

	/**
	 * 获取客户端id
	 *
	 * @param request request
	 * @return tenantId
	 */
	public static String getClientId(HttpServletRequest request) {
		CloudUser user = getUser(request);
		return (null == user) ? StringPool.EMPTY : user.getClientId();
	}

	/**
	 * 获取Claims
	 *
	 * @param request request
	 * @return Claims
	 */
	// 从HttpServletRequest请求中获取声明
	public static Claims getClaims(HttpServletRequest request) {
		// 获取请求头中的认证信息
		String auth = request.getHeader(SecureUtil.HEADER);
		// 如果认证信息不为空且长度大于AUTH_LENGTH
		if ((auth != null) && (auth.length() > AUTH_LENGTH)) {
			// 获取认证信息的前6位小写
			String headStr = auth.substring(0, 6).toLowerCase();
			// 如果前6位小写与BEARER比较
			if (headStr.compareTo(SecureUtil.BEARER) == 0) {
				// 截取认证信息从第7位开始
				auth = auth.substring(7);
				// 解析认证信息
				return SecureUtil.parseJWT(auth);
			}
		}
		// 如果认证信息不符合要求，返回null
		return null;
	}

	/**
	 * 获取请求头
	 *
	 * @return header
	 */
	public static String getHeader() {
		return getHeader(Objects.requireNonNull(WebUtil.getRequest()));
	}

	/**
	 * 获取请求头
	 *
	 * @param request request
	 * @return header
	 */
	public static String getHeader(HttpServletRequest request) {
		return request.getHeader(HEADER);
	}

	/**
	 * 解析jsonWebToken
	 *
	 * @param jsonWebToken jsonWebToken
	 * @return Claims
	 */
	/**
	 * 解析JWT
	 * @param jsonWebToken JWT
	 * @return 解析后的Claims
	 */
	public static Claims parseJWT(String jsonWebToken) {
		try {
			// 使用Jwts解析JWT
			return Jwts.parser()
				// 设置签名密钥
				.setSigningKey(Base64.getDecoder().decode(BASE64_SECURITY))
				// 解析JWT
				.parseClaimsJws(jsonWebToken).getBody();
		} catch (Exception ex) {
			// 如果解析出现异常，则返回null
			return null;
		}
	}

	/**
	 * 创建令牌
	 *
	 * @param user      user
	 * @param audience  audience
	 * @param issuer    issuer
	 * @param tokenType tokenType
	 * @return jwt
	 */
	public static TokenInfo createJWT(Map<String, String> user,
									  String audience,
									  String issuer,
									  String tokenType) {
		// 从请求头中提取客户端id和客户端密钥
		String[] tokens = extractAndDecodeHeader();
		assert tokens.length == 2;
		String clientId = tokens[0];
		String clientSecret = tokens[1];

		// 获取客户端信息
		IClientDetails clientDetails = clientDetails(clientId);

		// 校验客户端信息
	
		if (!validateClient(clientDetails, clientId, clientSecret)) {
			throw new SecureException("客户端认证失败!");
		}

		// 设置签名算法
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

		// 获取当前时间
		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);

		//生成签名密钥
		byte[] apiKeySecretBytes = Base64.getDecoder().decode(BASE64_SECURITY);
		Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

		//添加构成JWT的类
		JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JsonWebToken")
			.setIssuer(issuer)
			.setAudience(audience)
			.signWith(signatureAlgorithm, signingKey);

		//设置JWT参数
		user.forEach(builder::claim);

		//设置应用id
		builder.claim(CLIENT_ID, clientId);

		//添加Token过期时间
		long expireMillis;
		if (tokenType.equals(JwtConstant.ACCESS_TOKEN)) {
			// 设置access_token过期时间
			expireMillis = clientDetails.getAccessTokenValidity() * 1000;
		} else if (tokenType.equals(JwtConstant.REFRESH_TOKEN)) {
			// 设置refresh_token过期时间
			expireMillis = clientDetails.getRefreshTokenValidity() * 1000;
		} else {
			// 设置其他token过期时间
			expireMillis = getExpire();
		}
		long expMillis = nowMillis + expireMillis;
		Date exp = new Date(expMillis);
		builder.setExpiration(exp).setNotBefore(now);

		// 组装Token信息
		TokenInfo tokenInfo = new TokenInfo();
		tokenInfo.setToken(builder.compact());
		tokenInfo.setExpire((int) expireMillis / 1000);

		return tokenInfo;
	}


	/**
	 * 创建令牌
	 *
	 * @param user      user
	 * @param audience  audience
	 * @param issuer    issuer
	 * @param tokenType tokenType
	 * @param tenantId 租户id
	 * @return jwt
	 */
	public static TokenInfo createJWT(Map<String, String> user,
									  String audience,
									  String issuer,
									  String tokenType,
									  String tenantId
	) {
		// 从请求头中提取客户端id和客户端密钥
		String[] tokens = extractAndDecodeHeader();
		assert tokens.length == 2;
		String clientId = tokens[0];
		String clientSecret = tokens[1];

		// 获取客户端信息
		IClientDetails clientDetails = clientDetails(clientId,tenantId);

		// 校验客户端信息

		if (!validateClient(clientDetails, clientId, clientSecret)) {
			throw new SecureException("客户端认证失败!");
		}

		// 设置签名算法
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

		// 获取当前时间
		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);

		//生成签名密钥
		byte[] apiKeySecretBytes = Base64.getDecoder().decode(BASE64_SECURITY);
		Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

		//添加构成JWT的类
		JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JsonWebToken")
				.setIssuer(issuer)
				.setAudience(audience)
				.signWith(signatureAlgorithm, signingKey);

		//设置JWT参数
		user.forEach(builder::claim);

		//设置应用id
		builder.claim(CLIENT_ID, clientId);

		//添加Token过期时间
		long expireMillis;
		if (tokenType.equals(JwtConstant.ACCESS_TOKEN)) {
			// 设置access_token过期时间
			expireMillis = clientDetails.getAccessTokenValidity() * 1000;
		} else if (tokenType.equals(JwtConstant.REFRESH_TOKEN)) {
			// 设置refresh_token过期时间
			expireMillis = clientDetails.getRefreshTokenValidity() * 1000;
		} else {
			// 设置其他token过期时间
			expireMillis = getExpire();
		}
		long expMillis = nowMillis + expireMillis;
		Date exp = new Date(expMillis);
		builder.setExpiration(exp).setNotBefore(now);

		// 组装Token信息
		TokenInfo tokenInfo = new TokenInfo();
		tokenInfo.setToken(builder.compact());
		tokenInfo.setExpire((int) expireMillis / 1000);

		return tokenInfo;
	}

	/**
	 * 获取过期时间(次日凌晨3点)
	 *
	 * @return expire
	 */
	public static long getExpire() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, 1);
		cal.set(Calendar.HOUR_OF_DAY, 3);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTimeInMillis() - System.currentTimeMillis();
	}

	/**
	 * 客户端信息解码
	 */
	@SneakyThrows
	public static String[] extractAndDecodeHeader() {
		// 获取请求头客户端信息
		String header = Objects.requireNonNull(WebUtil.getRequest()).getHeader(SecureConstant.BASIC_HEADER_KEY);
		header = Func.toStr(header).replace(SecureConstant.BASIC_HEADER_PREFIX_EXT, SecureConstant.BASIC_HEADER_PREFIX);
		if (!header.startsWith(SecureConstant.BASIC_HEADER_PREFIX)) {
			throw new SecureException("缺少请求头"+SecureConstant.BASIC_HEADER_KEY);
		}
		byte[] base64Token = header.substring(6).getBytes(Charsets.UTF_8_NAME);

		byte[] decoded;
		try {
			decoded = Base64.getDecoder().decode(base64Token);
		} catch (IllegalArgumentException var7) {
			throw new RuntimeException("解码失败");
		}

		String token = new String(decoded, Charsets.UTF_8_NAME);
		int index = token.indexOf(StringPool.COLON);
		if (index == -1) {
			throw new RuntimeException("令牌无效");
		} else {
			return new String[]{token.substring(0, index), token.substring(index + 1)};
		}
	}

	/**
	 * 获取请求头中的客户端id
	 */
	public static String getClientIdFromHeader() {
		String[] tokens = extractAndDecodeHeader();
		assert tokens.length == 2;
		return tokens[0];
	}

	/**
	 * 获取客户端信息
	 *
	 * @param clientId 客户端id
	 * @return clientDetails
	 */
	private static IClientDetails clientDetails(String clientId) {
		return clientDetailsService.loadClientByClientId(clientId);
	}

	/**
	 * 获取客户端信息
	 *
	 * @param clientId 客户端id
	 * @param tenantId 租户ID
	 * @return clientDetails
	 */
	private static IClientDetails clientDetails(String clientId,String tenantId) {
		return clientDetailsService.loadClientByClientId(clientId,tenantId);
	}

	/**
	 * 校验Client
	 *
	 * @param clientId     客户端id
	 * @param clientSecret 客户端密钥
	 * @return boolean
	 */
	private static boolean validateClient(IClientDetails clientDetails, String clientId, String clientSecret) {
		if (clientDetails != null) {
			return StringUtil.equals(clientId, clientDetails.getClientId())
					&& StringUtil.equals(clientSecret, clientDetails.getClientSecret());
		}
		return false;
	}

}
