#pragma once

#include "EncodingType.h"
#include "encode/BaseGifEncoder.h"

class GifEncoder {
private:
	BaseGifEncoder* gifEncoder;
public:
	GifEncoder(EncodingType encodingType = NORMAL);

	bool init(uint16_t width, uint16_t height, const char* fileName);
	void release();
	void setDither(bool useDither);
	uint16_t getWidth();
	uint16_t getHeight();
	void setThreadCount(int32_t threadCount);

	void encodeFrame(uint32_t* pixels, int32_t delayMs);
};