# JPBC Implementation of H. Maji's ABS
This project is the implementation of Hemanta Maji's Attribute-based Signature (ABS) using Java Pairing-Based Cryptography Library (JPBC).


## Description
Attribute-based Signature (ABS) is a scheme to provide proof of possessions of attributes and provide authenticity of the data issuers. Combining with attribute-based encryption such as CP-ABE or KP-ABE, ABS is useful for different scenarios such as attribute-based messaging (ABM), secure voting and fine-grain user access control.

This ABS implementation is based on H. Maji's paper[1] and Sanjeev Kaushik Ramani's NDN-ABS project [2, 3]. For simplicity, this implementation only supported AND operations in the Monotone Span Program (MSP), and at most 10x10 AND matrix is pre-computed as `cheatyMap` to accelerate the encryption process. As a result, only less than or equal to 10 attributes is supported, but I guess it should be good enough for most scenarios.

This project supports both Type A (Symmetric) and Type D (Asymmetric) elliptic curve.

## Known Issues
- The attribute array used in `sign()` and `verify()` must follow the same order.
- Maximum 10 attributes are supported.
- Only AND operations are supported.


## Reference
[1] H. Maji, M. Prabhakaran, and M. Rosulek, “Attribute-Based Signatures: Achieving Attribute-Privacy and Collusion-Resistance,” in IACR Cryptology ePrint Archive, 2008. [http://eprint.iacr.org/2008/328](http://eprint.iacr.org/2008/328)

[2] S. K. Ramani, R. Tourani, G. Torres, S. Misra, and A. Afanasyev, “NDN-ABS: Attribute-based signature scheme for named data networking,” ICN 2019 - Proc. 2019 Conf. Information-Centric Netw., pp. 123–133, 2019. [https://named-data.net/publications/ndnabs/](https://named-data.net/publications/ndnabs/)

[3] Sanjeevr93, PyNDNABS: Python Implementation of NDN-ABS. [https://github.com/sanjeevr93/PyNDNABS](https://github.com/sanjeevr93/PyNDNABS)

[4] A. De Caro and V. Iovino, “jPBC: Java pairing based cryptography,” in Proceedings of the 16th IEEE Symposium on Computers and Communications, ISCC 2011, 2011, pp. 850–855. [http://gas.dia.unisa.it/projects/jpbc/](http://gas.dia.unisa.it/projects/jpbc/)


## Citation
Please feel free to use my project with the following citation:
> Man Chun Chow. _JPBC Implementation of H. Maji's ABS_. [https://github.com/cmcvista/JPBC-ABS2](https://github.com/cmcvista/JPBC-ABS2), 2019

