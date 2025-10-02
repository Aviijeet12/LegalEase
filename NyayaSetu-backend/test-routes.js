const express = require('express');

// Test importing each route file individually
console.log('Testing route imports...');

try {
  console.log('Importing auth routes...');
  const authRoutes = require('./routes/auth');
  console.log('✓ Auth routes imported successfully');
} catch (error) {
  console.error('✗ Auth routes import failed:', error.message);
}

try {
  console.log('Importing case routes...');
  const caseRoutes = require('./routes/cases');
  console.log('✓ Case routes imported successfully');
} catch (error) {
  console.error('✗ Case routes import failed:', error.message);
}

try {
  console.log('Importing document routes...');
  const documentRoutes = require('./routes/documents');
  console.log('✓ Document routes imported successfully');
} catch (error) {
  console.error('✗ Document routes import failed:', error.message);
}

try {
  console.log('Importing user routes...');
  const userRoutes = require('./routes/users');
  console.log('✓ User routes imported successfully');
} catch (error) {
  console.error('✗ User routes import failed:', error.message);
}

try {
  console.log('Importing chat routes...');
  const chatRoutes = require('./routes/chat');
  console.log('✓ Chat routes imported successfully');
} catch (error) {
  console.error('✗ Chat routes import failed:', error.message);
}

console.log('Route import test completed!');