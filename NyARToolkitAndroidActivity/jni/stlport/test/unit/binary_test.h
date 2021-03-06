// -*- C++ -*- Time-stamp: <09/01/23 00:51:38 ptr>

/*
 * Copyright (c) 2004-2009
 * Petr Ovtchenkov
 *
 * Copyright (c) 2004-2008
 * Francois Dumont
 *
 * Licensed under the Academic Free License Version 3.0
 *
 */

#ifndef __TEST_BINARY_TEST_H
#define __TEST_BINARY_TEST_H

#define __FIT_EXAM

#include <exam/suite.h>

class bsearch_test
{
  public:
    int EXAM_DECL(bsearch1);
    int EXAM_DECL(bsearch2);

  protected:
    static bool str_compare(const char* a_, const char* b_);
};

class bcompose_test
{
  public:
    int EXAM_DECL(bcompose1);
    int EXAM_DECL(bcompose2);
};

#endif // __TEST_BINARY_TEST_H
