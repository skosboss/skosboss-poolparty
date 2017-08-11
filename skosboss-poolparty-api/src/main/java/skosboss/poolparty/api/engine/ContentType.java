package skosboss.poolparty.api.engine;

import java.nio.charset.Charset;

class ContentType {
	
	private String mimeType;
	private Charset charset;
	
	ContentType(String mimeType, Charset charset) {
		this.mimeType = mimeType;
		this.charset = charset;
	}

	String getMimeType() {
		return mimeType;
	}
	
	Charset getCharset() {
		return charset;
	}

	@Override
	public String toString() {
		return "ContentType [mimeType=" + mimeType + ", charset=" + charset + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((charset == null) ? 0 : charset.hashCode());
		result = prime * result + ((mimeType == null) ? 0 : mimeType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		ContentType other = (ContentType) obj;
		if (charset == null) {
			if (other.charset != null) return false;
		}
		else if (!charset.equals(other.charset)) return false;
		if (mimeType == null) {
			if (other.mimeType != null) return false;
		}
		else if (!mimeType.equals(other.mimeType)) return false;
		return true;
	}
}