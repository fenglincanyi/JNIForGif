
#include <stdio.h>
#include <stdint.h>
#include "encode/BaseGifEncoder.h"
#include "encode/LCTGifEncoder.h"
#include "encode/FastGifEncoder.h"
#include "encode/SimpleGCTEncoder.h"
#include "encode/GCTGifEncoder.h"
#include "BitWritingBlock.h"
#include "EncodingType.h"
#include "GifEncoder.h"
#include <vector>

using namespace std;

GifEncoder::GifEncoder(EncodingType encodingType) {
    switch (encodingType) {
        case SIMPLE_FAST:
            gifEncoder = new SimpleGCTGifEncoder();
            break;
        case FAST:
            gifEncoder = new FastGifEncoder();
            break;
        case HIGH:
            gifEncoder = new GCTGifEncoder();
            break;
        case NORMAL:
        default:
            gifEncoder = new LCTGifEncoder();
            break;
    }
}

bool GifEncoder::init(uint16_t width, uint16_t height, const char *fileName) {
    return gifEncoder->init(width, height, fileName);
}

void GifEncoder::release() {
    gifEncoder->release();
}

void GifEncoder::setDither(bool useDither) {
    gifEncoder->setDither(useDither);
}

uint16_t GifEncoder::getWidth() {
    return gifEncoder->getWidth();
}

uint16_t GifEncoder::getHeight() {
    return gifEncoder->getHeight();
}

void GifEncoder::setThreadCount(int32_t threadCount) {
    gifEncoder->setThreadCount(threadCount);
}

void GifEncoder::encodeFrame(uint32_t *pixels, int delayMs) {
    gifEncoder->encodeFrame(pixels, delayMs);
}
