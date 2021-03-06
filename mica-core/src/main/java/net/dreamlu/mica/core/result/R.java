/*
 * Copyright (c) 2019-2029, Dreamlu 卢春梦 (596392912@qq.com & www.dreamlu.net).
 * <p>
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE 3.0;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.dreamlu.mica.core.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.dreamlu.mica.core.utils.ObjectUtil;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.util.Optional;

/**
 * 响应信息主体
 *
 * @param <T> 泛型标记
 * @author L.cm
 */
@Getter
@Setter
@ToString
@ApiModel(description = "返回信息")
@NoArgsConstructor
public class R<T> implements Serializable {
	private static final long serialVersionUID = -1160662278280275915L;

	@ApiModelProperty(value = "code值", required = true)
	private int code;
	@ApiModelProperty(value = "是否成功", required = true)
	private boolean success;
	@ApiModelProperty(value = "消息", required = true)
	private String msg;
	@ApiModelProperty("返回对象")
	private T data;

	private R(IResultCode resultCode) {
		this(resultCode, resultCode.getMsg(), null);
	}

	private R(IResultCode resultCode, String msg) {
		this(resultCode, msg, null);
	}

	private R(IResultCode resultCode, T data) {
		this(resultCode, resultCode.getMsg(), data);
	}

	private R(IResultCode resultCode, String msg, T data) {
		this.code = resultCode.getCode();
		this.msg = msg;
		this.data = data;
		this.success = SystemCode.SUCCESS == resultCode;
	}

	/**
	 * 判断返回是否为成功
	 *
	 * @param result Result
	 * @return 是否成功
	 */
	public static boolean isSuccess(@Nullable R<?> result) {
		return Optional.ofNullable(result)
			.map(x -> ObjectUtil.nullSafeEquals(SystemCode.SUCCESS.code, x.code))
			.orElse(Boolean.FALSE);
	}

	/**
	 * 判断返回是否为成功
	 *
	 * @param result Result
	 * @return 是否成功
	 */
	public static boolean isNotSuccess(@Nullable R<?> result) {
		return !R.isSuccess(result);
	}

	/**
	 * 获取data
	 * @param result Result
	 * @param <T> 泛型标记
	 * @return 泛型对象
	 */
	@Nullable
	public static <T> T getData(@Nullable R<T> result) {
		return Optional.ofNullable(result)
			.filter(r -> r.success)
			.map(x -> x.getData())
			.orElse(null);
	}

	/**
	 * 返回成功
	 * @param <T> 泛型标记
	 * @return Result
	 */
	public static <T> R<T> success() {
		return new R<>(SystemCode.SUCCESS);
	}

	/**
	 * 成功-携带数据
	 *
	 * @param data 数据
	 * @param <T> 泛型标记
	 * @return Result
	 */
	public static <T> R<T> success(@Nullable T data) {
		return new R<>(SystemCode.SUCCESS, data);
	}

	/**
	 * 根据状态返回成功或者失败
	 * @param status 状态
	 * @param msg 异常msg
	 * @param <T> 泛型标记
	 * @return Result
	 */
	public static <T> R<T> status(boolean status, String msg) {
		return status ? R.success() : R.fail(msg);
	}

	/**
	 * 根据状态返回成功或者失败
	 * @param status 状态
	 * @param sCode 异常code码
	 * @param <T> 泛型标记
	 * @return Result
	 */
	public static <T> R<T> status(boolean status, IResultCode sCode) {
		return status ? R.success() : R.fail(sCode);
	}

	/**
	 * 返回失败信息，用于 web
	 *
	 * @param msg 失败信息
	 * @param <T> 泛型标记
	 * @return {Result}
	 */
	public static <T> R<T> fail(String msg) {
		return new R<>(SystemCode.FAILURE, msg);
	}

	/**
	 * 返回失败信息
	 *
	 * @param rCode 异常枚举
	 * @param <T> 泛型标记
	 * @return {Result}
	 */
	public static <T> R<T> fail(IResultCode rCode) {
		return new R<>(rCode);
	}

	/**
	 * 返回失败信息
	 *
	 * @param rCode 异常枚举
	 * @param msg 失败信息
	 * @param <T> 泛型标记
	 * @return {Result}
	 */
	public static <T> R<T> fail(IResultCode rCode, String msg) {
		return new R<>(rCode, msg);
	}

}
